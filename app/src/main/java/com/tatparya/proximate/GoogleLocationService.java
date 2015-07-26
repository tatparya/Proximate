package com.tatparya.proximate;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseGeoPoint;

/**
 * Created by Tatparya_2 on 7/26/2015.
 */

//  GOOGLE API METHODS
public class GoogleLocationService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected ParseGeoPoint mGeoPoint;
    protected Location mLastLocation;
    protected GoogleApiClient mGoogleApiClient;
    protected Context mContext;

    //  Constructor
    GoogleLocationService( Context context ) {
        mContext = context;
        buildGoogleApiClient();
    }

    public ParseGeoPoint getLocation() {
        return mGeoPoint;
    }

    public void connectService(){
        if( servicesAvailable() )
        {
            mGoogleApiClient.connect();
        }
    }

    public void disconnectService() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d( ProximateApplication.LOGTAG, "Getting location" );
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient );
        if( mLastLocation != null )
        {
            Log.d(ProximateApplication.LOGTAG, "Latitude : " + String.valueOf(mLastLocation.getLatitude()));
            Log.d( ProximateApplication.LOGTAG, "Longitude : " + String.valueOf(mLastLocation.getLongitude()) );

            mGeoPoint = new ParseGeoPoint( mLastLocation.getLatitude(), mLastLocation.getLongitude() );
            //mCurrentUser.put( ParseConstants.KEY_USER_LOCATION, mGeoPoint );
            //mCurrentUser.saveInBackground();
        }
        else
        {
            Log.e( ProximateApplication.LOGTAG, "Failed to get location" );
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e( ProximateApplication.LOGTAG, "Connecting to Google Play Services failed" );
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e( ProximateApplication.LOGTAG, "Connection to Google Play Services suspended" );
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder( mContext )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi(LocationServices.API)
                .build();
    }

    private boolean servicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable( mContext );

        if( ConnectionResult.SUCCESS == resultCode )
        {
            Log.d( ProximateApplication.LOGTAG, "Google Play Services available!" );
            return true;
        }
        else
        {
            Toast.makeText( mContext, "Google Play Services is currently not available.\nPlease try again later!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
