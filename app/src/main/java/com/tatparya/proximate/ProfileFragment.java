package com.tatparya.proximate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tatparya_2 on 7/25/2015.
 */
public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View mRootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return mRootView;
    }

}
