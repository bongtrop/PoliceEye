package xyz.pongsakorn.policeeye.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import xyz.pongsakorn.policeeye.fragment.ResultTabFragment;

/**
 * Created by Porpeeranut on 25/2/2559.
 */
public class ResultPagerAdapter extends FragmentStatePagerAdapter {

    public final ArrayList<Fragment> fragments;
    Activity act;

    public ResultPagerAdapter(Activity act, FragmentManager fm, ArrayList<String> urlArray) {
        super(fm);
        this.act = act;
        fragments = new ArrayList<>();
        for(int i=0;i < urlArray.size();i++) {
            fragments.add(new ResultTabFragment(urlArray.get(i)));
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}