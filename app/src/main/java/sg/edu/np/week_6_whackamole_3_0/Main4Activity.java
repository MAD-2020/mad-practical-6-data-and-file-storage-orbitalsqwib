package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */

    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;

    private final List<Button> buttonList = new ArrayList<>();
    private static final int[] STANDARD_BUTTON_IDS = {
            R.id.button4, R.id.button6, R.id.button8
    };

    private static final int[] ADVANCED_BUTTON_IDS = {
            R.id.button1, R.id.button2, R.id.button3, R.id.button5,
            R.id.button7, R.id.button9
    };

    private TextView resultTextView;
    private Button backButton;

    private final List<Integer> CURRENT_BUTTON_LIST = new ArrayList<>();
    private MoleGame moleGame;

    private Integer levelNum;
    private String userName;

    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */

        readyTimer = new CountDownTimer(10*1000, 1000) {
            @Override
            public void onTick(long l) {
                Long timeRemainingInSeconds = l/1000;
                Log.v(TAG, "Ready CountDown!" + l/ 1000);
                String text = "Get ready in " + timeRemainingInSeconds.toString() + " seconds!";
                final Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                t.show();

                Timer killToast = new Timer();
                killToast.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        t.cancel();
                    }
                }, 1000);
            }

            @Override
            public void onFinish() {
                Log.v(TAG, "Ready CountDown Complete!");
                Toast.makeText(getApplicationContext(), "GO!", Toast.LENGTH_SHORT).show();
                placeMoleTimer();
                readyTimer.cancel();
            }
        };

        readyTimer.start();
    }

    private void placeMoleTimer(){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */

        newMolePlaceTimer = new CountDownTimer(moleGame.SECONDS_PER_REFRESH*1000,
                moleGame.SECONDS_PER_REFRESH*1000) {
            @Override
            public void onTick(long l) {
                Log.v(TAG, "New Mole Location!");
                moleGame.ResetAndRespawnMoles();
            }

            @Override
            public void onFinish() {
                newMolePlaceTimer.start();
            }
        };

        toggleButtonsEnabled(true);
        newMolePlaceTimer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */

        resultTextView = (TextView) findViewById(R.id.resultTextView);
        backButton = (Button) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();
                Intent returnToLevelSelect = new Intent(Main4Activity.this, Main3Activity.class);
                returnToLevelSelect.putExtra("CURRENT_USER_USERNAME", userName);
                startActivity(returnToLevelSelect);
            }
        });

        Intent dataReceiver = getIntent();
        levelNum = dataReceiver.getIntExtra("LEVEL_NUM", 1);
        userName = dataReceiver.getStringExtra("CURRENT_USER_USERNAME");

        Integer secondsPerRefresh = 10-levelNum+1;
        Integer molesPerRefresh = 1;
        Boolean advancedModeOn = false;
        if(levelNum > 5) {
            molesPerRefresh = 2;
            advancedModeOn = true;
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<CURRENT_BUTTON_LIST.size(); i++) {
                    if(CURRENT_BUTTON_LIST.get(i) == view.getId()) {
                        try{
                            newMolePlaceTimer.cancel();
                            moleGame.HandleHoleHit(i);
                            newMolePlaceTimer.start();
                        } catch (NullPointerException e) { }
                        break;
                    }
                }
            }
        };

        prepareButtonsForIDArray(STANDARD_BUTTON_IDS, listener);

        if(advancedModeOn) {
            prepareButtonsForIDArray(ADVANCED_BUTTON_IDS, listener);
        }

        resultTextView = (TextView) findViewById(R.id.resultTextView);

        moleGame = new MoleGame(buttonList, false, secondsPerRefresh, molesPerRefresh);
        moleGame.ResetAllMoles();
        moleGame.LinkTextViewAsResultView(resultTextView);

        toggleButtonsEnabled(false);
        readyTimer();
    }

    private void updateUserScore()
    {
     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
        Log.v(TAG, FILENAME + ": Update User Score...");
        try{
            newMolePlaceTimer.cancel();
            readyTimer.cancel();
        } catch (NullPointerException e) { }

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        UserData newUserData = dbHandler.findUser(userName);
        newUserData.getScores().set(levelNum-1, moleGame.getScore());

        //Replace user data
        dbHandler.deleteAccount(userName);
        dbHandler.addUser(newUserData);
    }

    private void prepareButtonsForIDArray(int[] buttonsIDList, View.OnClickListener listener) {
        for(final int id : buttonsIDList){
            Button b = (Button) findViewById(id);
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(listener);
            buttonList.add(b);
            CURRENT_BUTTON_LIST.add(id);
        }
    }

    private void toggleButtonsEnabled(boolean enabled) {
        for(Button b: buttonList) {
            //b.setEnabled(enabled);
        }
    }

}
