package com.tatparya.proximate;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends ActionBarActivity {

    protected TextView mSignUpTextView;
    protected EditText mUsername;
    protected EditText mPassword;
    protected Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //  Find views
        mUsername = ( EditText ) findViewById( R.id.usernameField );
        mPassword = ( EditText ) findViewById( R.id.passwordField );
        mLoginButton = ( Button ) findViewById( R.id.loginButton );

        //  Set click listener on login button
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Get user inputs
                getUserInputs();
            }
        });

        //  Set click listener on Sign Up Text
        mSignUpTextView = ( TextView ) findViewById( R.id.signUpText );
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Start Sign Up Activity
                Intent intent = new Intent( LoginActivity.this, SignUpActivity.class );
                startActivity(intent);
            }
        });
    }

    //  ** Get user inputs, validate and send to Parse **
    protected void getUserInputs()
    {
        //  ** Get user inputs **
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        //  Trim any whitespaces
        username = username.trim();
        password = password.trim();

        //  Validate inputs
        if( username.isEmpty() || password.isEmpty() )
        {
            //  ** Display error message **
            AlertDialog.Builder builder = new AlertDialog.Builder( LoginActivity.this );
            builder.setMessage( R.string.empty_field_error_message )
                    .setTitle( R.string.error_title )
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            //  ** Log In User **
            Log.d( ProximateApplication.LOGTAG, "Logging in user" );
            //  Show progress indicator
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                //  Call Back when sign up completes
                // ** Note : returns Null user if fails
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if( e == null )
                    {
                        //  Logged in
                        Log.d( ProximateApplication.LOGTAG, "Logged in user successfully!!" );
                        Toast.makeText(LoginActivity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                        //  Launch Main Activity
                        Intent intent = new Intent( LoginActivity.this, MainActivity.class );
                        //  To clear the activity stack
                        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity( intent );
                    }
                    else
                    {
                        //  Error logging in
                        Log.d( ProximateApplication.LOGTAG, "Logging in failed!!" );
                        //  Sign up failed
                        AlertDialog.Builder builder = new AlertDialog.Builder( LoginActivity.this );
                        builder.setTitle( R.string.error_title )
                                .setMessage( "Could not log in\nError : " + e.getMessage() )
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });
        }
    }
}
