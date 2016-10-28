package in.voiceme.app.voiceme.DiscoverPage;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Harish on 7/29/2016.
 */

public class DiscoverActivityFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    ArrayList<Fragment> pages = new ArrayList<>();
    private String tabTitles[] = new String[]{"Latest", "Trending", "Popular"};

    public DiscoverActivityFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return DiscoverLatestFragment.newInstance(0);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return DiscoverTrendingFragment.newInstance(1);
            case 2: // Fragment # 1 - This will show SecondFragment
                return DiscoverPopularFragment.newInstance(2);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    //add a page
    public void addPage(Fragment fragment) {
        pages.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
