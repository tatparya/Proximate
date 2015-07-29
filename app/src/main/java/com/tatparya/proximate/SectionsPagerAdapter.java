package com.tatparya.proximate;

import android.content.Context;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Created by Tatparya_2 on 7/25/2015.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        //  Return required fragment based on the position
        Fragment newFragment;
        switch ( position ){
            case 0:
                newFragment = new ProfileFragment();
                break;
            default:
                newFragment = new NearbyFragment();
        }
        return newFragment;
    }

    @Override
    public int getCount() {
        //  Show two total pages
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_profile).toUpperCase(l);
            default:
                return mContext.getString(R.string.title_nearby).toUpperCase(l);
        }
    }
}
