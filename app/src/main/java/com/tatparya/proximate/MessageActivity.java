package com.tatparya.proximate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        MessageActivityFragment fragment = ( MessageActivityFragment )
                getFragmentManager().findFragmentById( R.id.messageFragment );

        Intent intent = getIntent();
        String recepientId = intent.getStringExtra( ParseConstants.KEY_RECIPIENT_ID );
        String username = intent.getStringExtra( ParseConstants.KEY_USERNAME );
        fragment.setRecepientId( recepientId, username );
    }
}
