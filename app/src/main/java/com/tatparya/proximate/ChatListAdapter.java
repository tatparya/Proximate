package com.tatparya.proximate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tatparya_2 on 7/27/2015.
 */
public class ChatListAdapter extends ArrayAdapter<Message> {

    private String mUserId;
    private Context mContext;

    public ChatListAdapter( Context context, String userId, List<Message> messages ){
        super( context, 0, messages );
        this.mUserId = userId;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = (Message) getItem( position );

//        Log.d(ProximateApplication.LOGTAG, "User logged in : " + mUserId
//        + ", message from : " + message.getSenderName() + ", message : "
//        + message.getBody());
        if( message.getSenderName().equals(mUserId) )
        {
            // Sent message
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.chat_item_right, parent, false);
        }
        else
        {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.chat_item_left, parent, false);
        }
        final ViewHolder holder = new ViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.username_text);
        holder.body = (TextView) convertView.findViewById(R.id.body_text);
        convertView.setTag(holder);

        holder.name.setText( message.getSenderName() );
        holder.body.setText( message.getBody() );
        return convertView;
    }

    public final class ViewHolder{
        public TextView name;
        public TextView body;
    }
}
