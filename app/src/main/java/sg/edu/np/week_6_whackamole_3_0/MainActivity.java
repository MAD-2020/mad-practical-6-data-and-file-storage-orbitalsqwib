package sg.edu.np.week_6_whackamole_3_0;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */

    // View objects
    private EditText UsernameEditText;
    private EditText PasswordEditText;
    private Button LoginButton;
    private Button SignUpButton;

    private MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind View objects
        UsernameEditText = (EditText) findViewById(R.id.usernameEditText);
        PasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        LoginButton = (Button) findViewById(R.id.loginButton);
        SignUpButton = (Button) findViewById(R.id.signUpButton);

        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");
        */

        // Set listeners
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernameInput = UsernameEditText.getText().toString();
                String passwordInput = PasswordEditText.getText().toString();

                Log.v(TAG, FILENAME + ": Logging in with: " +
                        usernameInput + ": " + passwordInput);

                if(!isValidUser(usernameInput, passwordInput)) {
                    Log.v(TAG, FILENAME + ": Invalid user!");
                    return;
                }

                Log.v(TAG, FILENAME + ": Valid User! Logging in");

                // Log in
                Intent toLevelSelect = new Intent(MainActivity.this, Main3Activity.class);
                // Pass username through Intent
                toLevelSelect.putExtra("CURRENT_USER_USERNAME", usernameInput);
                startActivity(toLevelSelect);
                finish();
            }
        });

        SignUpButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(TAG, FILENAME + ": Create new user!");
                Intent goToSignUp = new Intent(MainActivity.this, Main2Activity.class);
                startActivityForResult(goToSignUp, REQUEST_CODE);
                return true;
            }
        });

    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */
        UserData userData = dbHandler.findUser(userName);

        if (userData == null) {
            // Fail username check
            displayError(UsernameEditText, "Can't find an account with that username!");
            return false;
        }

        Log.v(TAG, FILENAME + ": Running Checks..." + userData.getMyUserName() + ": " +
                userData.getMyPassword() +" <--> "+ userName + " " + password);

        if (userData.getMyPassword().equals(password) == false) {
            // Fail password check
            displayError(PasswordEditText, "Incorrect password!");
            return false;
        }

        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void displayError(EditText editText, String errorMsg) {
        editText.setError(errorMsg);
        Toast.makeText(this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
        PasswordEditText.setText("");
    }

}
