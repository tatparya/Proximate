package com.tatparya.proximate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.parse.ParseUser;

public class WelcomeActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                ParseUser currentUser = ParseUser.getCurrentUser();

                if( currentUser == null )
                {
                    //  Login
                    startLoginActivity();
                }
                else
                {
                    //  Main Activity
                    startMainActivity();
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    //  Start Login Activity
    private void startLoginActivity() {
        //  Start Login Activity
        Log.d(ProximateApplication.LOGTAG, "Starting loginActivity");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //  Start Login Activity
    private void startMainActivity() {
        //  Start Login Activity
        Log.d(ProximateApplication.LOGTAG, "Starting mainActivity");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
