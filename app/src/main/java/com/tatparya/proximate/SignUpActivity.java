package com.tatparya.proximate;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends ActionBarActivity {

    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mPasswordConfirm;
    protected EditText mEmail;
    protected Button mSignUpButton;
    protected ParseGeoPoint point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //  Find views
        mUsername = ( EditText ) findViewById( R.id.usernameField );
        mPassword = ( EditText ) findViewById( R.id.passwordField );
        mPasswordConfirm = ( EditText ) findViewById( R.id.passwordConfirmField );
        mEmail = ( EditText ) findViewById( R.id.emailField );
        mSignUpButton = ( Button ) findViewById( R.id.signUpButton );

        //  Set click listener to button
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Get user inputs
                getUserInputs();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //  ** Get user inputs, validate and send to Parse **
    protected void getUserInputs()
    {
        //  ** Get user inputs **
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        String passwordConfirm = mPasswordConfirm.getText().toString();
        String email = mEmail.getText().toString();

        //  Trim any whitespaces
        username = username.trim();
        password = password.trim();
        passwordConfirm = passwordConfirm.trim();
        email = email.trim();

        Log.d( ProximateApplication.LOGTAG, "Email : " + email );

        //  Validate inputs and save user
        if( username.isEmpty() || password.isEmpty() || email.isEmpty() || passwordConfirm.isEmpty() )
        {
            //  ** Display error message **
            AlertDialog.Builder builder = new AlertDialog.Builder( SignUpActivity.this );
            builder.setMessage( R.string.empty_field_error_message )
                    .setTitle( R.string.error_title )
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if( !password.equals( passwordConfirm ) )
        {
            //  ** Display error message **
            AlertDialog.Builder builder = new AlertDialog.Builder( SignUpActivity.this );
            builder.setMessage( R.string.password_doesnt_match_message )
                    .setTitle( R.string.error_title )
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            //  ** Save user to Parse **
            Log.d( ProximateApplication.LOGTAG, "Saving New User" );
            ParseUser newUser = new ParseUser();
            //  Set fields
            newUser.setUsername( username );
            newUser.setEmail( email );
            newUser.setPassword( password );
            point = ProximateApplication.mGoogleLocationService.getLocation();
            newUser.put( ParseConstants.KEY_USER_LOCATION, point );
            Log.d( ProximateApplication.LOGTAG, "Got user location!" );
            //  Sign Up User
            newUser.signUpInBackground(new SignUpCallback() {
                //  Call Back when sign up completes
                // ** Note : returns Null if success
                @Override
                public void done(ParseException e) {
                    //  Hide progress indicator
                    //  Check if sign up success
                    if (e == null) {
                        Log.d(ProximateApplication.LOGTAG, "Saved user successfully!!");
                        Toast.makeText(SignUpActivity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                        //  Launch Main Activity
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        //  To clear the activity stack
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Log.d(ProximateApplication.LOGTAG, "Saving user failed!!");
                        //  Sign up failed
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setTitle(R.string.error_title);
                        builder.setMessage("Sign up failed, please try again!\nError : " + e.getMessage());
                        builder.setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });
        }
    }
}
