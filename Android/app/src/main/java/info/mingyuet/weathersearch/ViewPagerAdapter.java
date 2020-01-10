package info.mingyuet.weathersearch;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

class ViewPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private List<Fragment> fragments = new ArrayList<>();

    private String title[] = {"Today", "Weekly", "PhotoTab"};

    public ViewPagerAdapter(FragmentManager manager, String weatherDetail, String address) {
        super(manager);
        fragments.add(TodayTab.getInstance(0, weatherDetail));
        fragments.add(WeeklyTab.newInstance(weatherDetail));
        fragments.add(PhotoTab.newInstance(address));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
