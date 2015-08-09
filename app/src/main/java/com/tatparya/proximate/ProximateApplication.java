package com.tatparya.proximate;

import android.app.Application;
import android.util.Log;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Tatparya_2 on 7/25/2015.
 */


//  ** NOTE : Entry Point to application
public class ProximateApplication extends Application {

    //  Some constant string definitions
    public static final String APPNAME  =   "Proximate";
    public static final String LOGTAG   =   "DebugLog";
    public static GoogleLocationService mGoogleLocationService;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(LOGTAG, "Hello World!!");
        //  API initializations to be done HERE ~

        // Register your parse models here
        ParseObject.registerSubclass(Message.class);
        //  ** Setting up Parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "kBvdtUIa0x3OaBzL1KbsN7rqzBEj0evV85UkUvNZ", "QonhJQPW6olGk5CU5DwHQ1bAXcklTk07yeaFF3MQ");

        //  Subscribe to Parse Push Service
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

        mGoogleLocationService = new GoogleLocationService( getApplicationContext() );
        mGoogleLocationService.connectService();


    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mGoogleLocationService.disconnectService();
    }
}
