package xyz.pongsakorn.policeeye.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import xyz.pongsakorn.policeeye.fragment.ResultTabFragment;
import xyz.pongsakorn.policeeye.utils.SketchMatchSDK;

/**
 * Created by Porpeeranut on 25/2/2559.
 */
public class ResultPagerAdapter extends FragmentStatePagerAdapter {

    public final ArrayList<Fragment> fragments;
    Activity act;

    public ResultPagerAdapter(Activity act, FragmentManager fm, ArrayList<SketchMatchSDK.Person> people) {
        super(fm);
        this.act = act;
        fragments = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        int c = 0;
        for(int i=0;i < people.size();i++) {
            if (c==5) break;

            if (ids.indexOf(people.get(i).id)>-1 || people.get(i).point==0) continue;

            fragments.add(new ResultTabFragment(people.get(i)));
            ids.add(people.get(i).id);
            c++;
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