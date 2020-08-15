package controler;

import android.content.Context;

import com.miguel.go4lunch_p6.ListViewFragment;
import com.miguel.go4lunch_p6.MapViewFragment;
import com.miguel.go4lunch_p6.R;
import com.miguel.go4lunch_p6.WorkmatesFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public FragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return(3);
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
