package com.miguel.go4lunch_p6;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private JsonResponse mJsonResponse;

    public FragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return(3); // 3 - Number of page to show
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: //Page number 1
                return MapViewFragment.newInstance();
            case 1: //Page number 2
                return ListViewFragment.newInstance();
            case 2: //Page number 3
                return WorkmatesFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: //Page number 1
                return mContext.getResources().getString(R.string.tab_label1);
            case 1: //Page number 2
                return mContext.getResources().getString(R.string.tab_label2);
            case 2: //Page number 3
                return mContext.getResources().getString(R.string.tab_label3);
            default:
                return null;
        }
    }
}
