package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Tian on 2017/10/30.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> listTitles;

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> listTitles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.listTitles = listTitles;
    }

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitles.get(position);
    }
}
