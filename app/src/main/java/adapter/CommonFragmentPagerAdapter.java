package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Tian on 2017/10/31.
 * 表情，图片，拍摄的适配器
 */

public class CommonFragmentPagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> fragmentList;

    public CommonFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public CommonFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList!=null? fragmentList.size() : 0;
    }
}
