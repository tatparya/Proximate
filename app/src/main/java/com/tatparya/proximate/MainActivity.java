package com.tatparya.proximate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.parse.ParseAnalytics;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class MainActivity extends Activity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    ViewPager mViewPager;

    //  VARIABLE DECLARATIONS
    protected ParseUser mCurrentUser;
    private ParseGeoPoint mGeoPoint;

    //  ACTIVITY METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Parse Analytics
        ParseAnalytics.trackAppOpened(getIntent());

        //  ** Check if User is logged in **
        mCurrentUser = ParseUser.getCurrentUser();
        if (mCurrentUser != null) {
            // User logged in
            Log.d(ProximateApplication.LOGTAG, "User logged in : " + mCurrentUser.getUsername());
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put( ParseConstants.KEY_USERNAME, ParseUser.getCurrentUser().getUsername() );
            installation.put( ParseConstants.CLASS_USER, ParseUser.getCurrentUser() );
            installation.saveInBackground();
        } else {
            //  User not logged in, Start Login Activity
            Log.d(ProximateApplication.LOGTAG, "No user logged in, starting loginActivity");
            startLoginActivity();
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //  Update location
        mGeoPoint = ProximateApplication.mGoogleLocationService.getLocation();
        if( mGeoPoint != null )
        {
            mCurrentUser.put( ParseConstants.KEY_USER_LOCATION, mGeoPoint );
            mCurrentUser.saveInBackground();
        }
        else
        {
            Log.e( ProximateApplication.LOGTAG, "Failed to find location : Main Activity");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //  OPTIONS MENU METHODS

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        else if (id == R.id.action_logout) {
            //  Log Out User
            ParseUser.logOut();
            Toast.makeText(MainActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
            startLoginActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    //  CUSTOM METHODS

    //  Start Login Activity
    private void startLoginActivity() {
        //  Start Login Activity
        Log.d(ProximateApplication.LOGTAG, "Starting loginActivity");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
