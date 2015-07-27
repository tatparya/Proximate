package com.tatparya.proximate;

import android.app.Application;
import android.util.Log;
import com.parse.Parse;
import com.parse.ParseObject;

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

        Log.d( LOGTAG, "Hello World!!" );
        //  API initializations to be done HERE ~

        // Register your parse models here
        ParseObject.registerSubclass(Message.class);
        //  ** Setting up Parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "kBvdtUIa0x3OaBzL1KbsN7rqzBEj0evV85UkUvNZ", "QonhJQPW6olGk5CU5DwHQ1bAXcklTk07yeaFF3MQ");

        mGoogleLocationService = new GoogleLocationService( getApplicationContext() );
        mGoogleLocationService.connectService();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mGoogleLocationService.disconnectService();
    }
}
