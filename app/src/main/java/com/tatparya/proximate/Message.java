package com.tatparya.proximate;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Tatparya_2 on 7/27/2015.
 */

@ParseClassName("Message")
public class Message extends ParseObject {

    public String getRecepientName(){
        return getString(ParseConstants.KEY_RECIPIENT_NAME);
    }

    public String getSenderName(){
        return getString(ParseConstants.KEY_SENDER_NAME);
    }

    public String getBody() {
        return getString(ParseConstants.KEY_MESSAGE_BODY);
    }

    public void setRecepientName(String userId) {
        put(ParseConstants.KEY_RECIPIENT_NAME, userId);
    }

    public void setMessageBody(String body) {
        put(ParseConstants.KEY_MESSAGE_BODY, body);
    }

    public void setSenderName(String username){
        put(ParseConstants.KEY_SENDER_NAME, username);
    }

}
