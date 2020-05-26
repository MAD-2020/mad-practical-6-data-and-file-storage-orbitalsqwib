package sg.edu.np.week_6_whackamole_3_0;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MoleGame {
    
    private static final String TAG = "Whack-A-Mole";

    public List<ButtonHole> holeList = new ArrayList<>();

    private TextView resultView;
    private Integer numberOfHoles;
    private Integer score;

    private Boolean LOG_BUTTON_NAME;
    public Integer SECONDS_PER_REFRESH;
    private Integer MOLES_PER_REFRESH;

    public MoleGame(List<Button> holeButtonList, Boolean logButtonName,
                    Integer secondsPerRefresh, Integer molesPerRefresh) {
        this.score = 0;
        this.numberOfHoles = holeButtonList.size();
        this.LOG_BUTTON_NAME = logButtonName;
        this.SECONDS_PER_REFRESH = secondsPerRefresh;
        this.MOLES_PER_REFRESH = molesPerRefresh;

        for(int i=0; i<numberOfHoles; i++) {
            this.holeList.add(new ButtonHole(holeButtonList.get(i)));
        }

        ResetAndRespawnMoles();
    }

    public Integer getScore() { return this.score;}

    private void setScore(int value) {
        this.score = value;
        UpdateResultViewToMatchModel();
    }

    public void ResetAllMoles() {
        for (ButtonHole hole: holeList) {
            hole.setHasMole(false);
        }
    }

    public void ResetAndRespawnMoles() {
        ResetAllMoles();

        List<Integer> hasMoles = new ArrayList<>();
        for(int i=0; i<MOLES_PER_REFRESH; i++) {
            Integer randomHoleIndex = (int)(Math.random() * numberOfHoles);
            while (hasMoles.contains(randomHoleIndex)) {
                randomHoleIndex = (int)(Math.random() * numberOfHoles);
            }
            hasMoles.add(randomHoleIndex);
            holeList.get(randomHoleIndex).SpawnMole();
        }

    }

    public void HandleHoleHit(int holeIndex) {
        boolean hitMole = holeList.get(holeIndex).getHasMole();
        ResetAndRespawnMoles();
        System.out.print("Whack-A-Mole: ");
        if (LOG_BUTTON_NAME) {
            switch (holeIndex) {
                case 0:
                    Log.v(TAG, "Button Left Clicked!");
                    break;
                case 1:
                    Log.v(TAG, "Button Middle Clicked!");
                    break;
                case 2:
                    Log.v(TAG, "Button Right Clicked!");
                    break;
                default:
                    Log.v(TAG, "Unknown Button Clicked! Did you forget to add cases for new buttons?");
            }
        }
        if (hitMole) {
            setScore(score+1);
            Log.v(TAG, "Hit, score added!\n");
        } else {
            setScore(score-1);
            Log.v(TAG, "Missed, score deducted!\n");
        }
    }

    public void LinkTextViewAsResultView(TextView t) {
        this.resultView = t;
    }

    public void UpdateResultViewToMatchModel() {
        resultView.setText(score.toString());
    }
}
