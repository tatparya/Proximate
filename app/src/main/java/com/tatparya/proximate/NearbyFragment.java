package com.tatparya.proximate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.parse.*;

import java.util.List;

/**
 * Created by Tatparya_2 on 7/25/2015.
 */
public class NearbyFragment extends Fragment {

    protected List<ParseUser> mUsers;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected ParseGeoPoint mUserLocation;
    protected ArrayAdapter<String> mListAdapter;
    protected ListView mListView;
    protected TextView emptyTextView;
    protected Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View mRootView = inflater.inflate(R.layout.fragment_nearby, container, false);
        mCurrentUser = ParseUser.getCurrentUser();
        mListView = (ListView) mRootView.findViewById(R.id.nearbyListView);
        emptyTextView = (TextView) mRootView.findViewById(R.id.emptyListView);
        mContext = getActivity();
        if( mCurrentUser != null )
        {
            mUserLocation = (ParseGeoPoint) mCurrentUser.get( ParseConstants.KEY_USER_LOCATION );
        }

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        //  ** GET ALL USERS NEARBY AND POPULATE LIST **
        mCurrentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNear( ParseConstants.KEY_USER_LOCATION, mUserLocation );
        query.setLimit(100);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if( e == null )
                {
                    //  Success : Extract user names and set to list
                    setUsersToList( list );
                }
                else
                {
                    //  Failed : Show error message
                    Toast.makeText(mContext, "Failed to find people nearby.\nPlease try again later!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUsersToList( List<ParseUser> list )
    {
        mUsers = list;
        if( mUsers.size() > 0 )
        {
            emptyTextView.setVisibility(View.GONE);
        }
        else
        {
            emptyTextView.setVisibility(View.VISIBLE);
        }
        //  Get list of usernames
        String[] usernames = new String[ mUsers.size() ];
        int i = 0;
        for( ParseUser user : mUsers )
        {
            usernames[i] = user.getUsername();
            i++;
        }
        //  Set to listview
        mListAdapter = new ArrayAdapter<String>(
                //  Context
                mContext,
                //  View Layout
                android.R.layout.simple_list_item_1,
                //  Data
                usernames
        );
        mListView.setAdapter( mListAdapter );
        //  Set onClick Listener
        mListView.setOnItemClickListener( messageSelectedUser() );
    }

    @NonNull
    private AdapterView.OnItemClickListener messageSelectedUser() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //  Get reciever username and start message activity
                String receiver = mUsers.get( position ).getUsername();
                Log.d( ProximateApplication.LOGTAG, "Messaging : " + receiver );
                Intent intent = new Intent( mContext, MessageActivity.class );
                intent.putExtra( ParseConstants.KEY_RECIPIENT_NAME, receiver );
                startActivity( intent );
            }
        };
    }
}
