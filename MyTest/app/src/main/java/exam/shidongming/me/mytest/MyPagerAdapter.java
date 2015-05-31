package exam.shidongming.me.mytest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> myFragmentList;

    public MyPagerAdapter(android.support.v4.app.FragmentManager fm, List<Fragment> list) {
        super(fm);
        myFragmentList = list;
    }


    @Override
    public int getCount() {
        return myFragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return myFragmentList.get(position);
    }
}
