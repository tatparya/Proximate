package com.tatparya.proximate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

/**
 * Created by Tatparya_2 on 7/25/2015.
 */
public class InboxFragment extends Fragment {

    protected ParseUser mCurrentUser;
    protected ParseGeoPoint mUserLocation;
    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();

        //  Retrieve location values
        mUserLocation = (ParseGeoPoint) mCurrentUser.get( ParseConstants.KEY_USER_LOCATION );
        TextView longitudeText = ( TextView ) mRootView.findViewById( R.id.longitude_text );
        TextView latitudeText = ( TextView ) mRootView.findViewById( R.id.latitude_text );
        TextView usernameText = ( TextView ) mRootView.findViewById( R.id.username_text );
        if( mUserLocation != null )
        {
            longitudeText.setText( String.valueOf( mUserLocation.getLongitude() ) );
            latitudeText.setText( String.valueOf( mUserLocation.getLatitude() ) );
            usernameText.setText( mCurrentUser.getUsername() );
        }
        else
        {
            longitudeText.setText( "Not available" );
            latitudeText.setText( "Not available" );
        }

    }
}
