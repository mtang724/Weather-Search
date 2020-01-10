package info.mingyuet.weathersearch;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class PlansPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    List<String> locations;
    private long baseId = 0;
    public PlansPagerAdapter(FragmentManager fm, int NumOfTabs, List<String> locations) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.locations = locations;
    }

    @Override
    public Fragment getItem(int position) {
        if (position > 0) {
            return DynamicFragment.newInstance(position, locations.get(position - 1).replace("#", ", "));
        } else {
            return DynamicFragment.newInstance(position, "");
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

}