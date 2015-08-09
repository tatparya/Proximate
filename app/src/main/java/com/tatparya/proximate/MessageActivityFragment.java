package com.tatparya.proximate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageActivityFragment extends Fragment {

    public boolean active = false;

    protected String mRecepientId;
    protected String mRecepientName;
    protected ParseUser mCurrentUser;
    protected ParseUser recepientUser;
    protected EditText messageEditText;
    protected Button sendButton;
    protected View mRootView;
    protected ListView chatListView;
    protected ChatListAdapter mAdapter;
    protected ArrayList<Message> mMessages;
    protected Context mContext;
    protected NotificationListener mMessageReceiver;

    public MessageActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_message, container, false);
        messageEditText = (EditText) mRootView.findViewById( R.id.messageEditText );
        sendButton = (Button) mRootView.findViewById( R.id.sendButton );
        chatListView = (ListView) mRootView.findViewById( R.id.chat_listView );
        mContext = getActivity();
        mCurrentUser = ParseUser.getCurrentUser();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMessageReceiver = new NotificationListener();
        IntentFilter filter = new IntentFilter( "com.parse.push.intent.RECEIVE" );
        mContext.registerReceiver( mMessageReceiver, filter );
        active = true;
        sendButton.setOnClickListener(sendMessage());

        //  Populate chat list
        mMessages = new ArrayList<Message>();
        mAdapter = new ChatListAdapter( mContext,
                mCurrentUser.getUsername(), mMessages);
        chatListView.setAdapter( mAdapter );
        receiveMessage();
    }

    @Override
    public void onPause() {
        super.onPause();
        active = false;
        mContext.unregisterReceiver(mMessageReceiver);
    }

    @NonNull
    private View.OnClickListener sendMessage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = messageEditText.getText().toString().trim();
                if( !data.isEmpty() )
                {
                    //  Send message
                    messageEditText.setText( "" );
                    Log.d( ProximateApplication.LOGTAG, "Sending message : " + data );
                    //  Save message to parse database
                    Message message = new Message();
                    message.setSenderName(mCurrentUser.getUsername());
                    message.setRecepientName(mRecepientName);
                    message.setMessageBody(data);
                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if( e == null )
                            {
                                // Success
//                                Toast.makeText( mContext, "Message sent successfully!", Toast.LENGTH_SHORT ).show();
                                //  TODO : Make this more efficient
                                receiveMessage();
                                //  Increase user score
                                int score = (int) mCurrentUser.get( ParseConstants.KEY_USER_SCORE );
                                mCurrentUser.put( ParseConstants.KEY_USER_SCORE, score + 1 );
                                mCurrentUser.saveInBackground();
                            }
                        }
                    });
                    //  Send push notification to other user
                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    Log.d( ProximateApplication.LOGTAG, "Seding push, " + ParseConstants.CLASS_USER + " : " + mRecepientId );
                    pushQuery.whereEqualTo( ParseConstants.KEY_USERNAME, mRecepientName );
                    ParsePush push = new ParsePush();
                    push.setQuery( pushQuery );
                    push.setMessage(mCurrentUser.getUsername() + " sent you a message!");
                    push.sendInBackground(new SendCallback() {
                        @Override
                        public void done(ParseException e) {
                            if( e == null )
                            {
                                Log.d( ProximateApplication.LOGTAG, "Push sent successfully!" );
                            }
                            else
                            {
                                Log.d( ProximateApplication.LOGTAG, "Push could not be sent : " + e.getMessage() );
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText( mContext, "Please type a message!", Toast.LENGTH_SHORT ).show();
                }
            }
        };
    }

    private void receiveMessage(){
        Toast.makeText( mContext, "Fetching messages...", Toast.LENGTH_SHORT ).show();

        ParseQuery<Message> queryIncoming = ParseQuery.getQuery(Message.class);
        queryIncoming.whereEqualTo( ParseConstants.KEY_RECIPIENT_NAME, mCurrentUser.getUsername() );
        queryIncoming.whereEqualTo( ParseConstants.KEY_SENDER_NAME, mRecepientName );

        ParseQuery<Message> queryOutgoing = ParseQuery.getQuery(Message.class);
        queryOutgoing.whereEqualTo( ParseConstants.KEY_RECIPIENT_NAME, mRecepientName );
        queryOutgoing.whereEqualTo( ParseConstants.KEY_SENDER_NAME, mCurrentUser.getUsername() );

        List<ParseQuery<Message>> queries = new ArrayList<ParseQuery<Message>>();
        queries.add( queryIncoming );
        queries.add( queryOutgoing );

        ParseQuery<Message> mainQuery = ParseQuery.or(queries);
        mainQuery.orderByAscending( ParseConstants.KEY_CREATED_AT );
        mainQuery.setLimit(50);
        mainQuery.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> list, ParseException e) {
                if( e == null )
                {
                    mMessages.clear();
                    mMessages.addAll( list );
                    mAdapter.notifyDataSetChanged();
                    chatListView.invalidate();
                }
            }
        });
    }

    public void setRecepientId( String id, String name )
    {
        mRecepientId = id;
        mRecepientName = name;
    }

    private class NotificationListener extends ParsePushBroadcastReceiver {
        @Override
        protected void onPushReceive(Context context, Intent intent) {
            super.onPushReceive(context, intent);

            Log.d(ProximateApplication.LOGTAG, "Broadcast Received!!");
            receiveMessage();
        }
    }
}
