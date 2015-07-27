package com.tatparya.proximate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.parse.ParseUser;

/**
 * Created by Tatparya_2 on 7/25/2015.
 */
public class InboxFragment extends Fragment {

    protected ParseUser mCurrentUser;
    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        mCurrentUser = ParseUser.getCurrentUser();
        return mRootView;
    }
}
