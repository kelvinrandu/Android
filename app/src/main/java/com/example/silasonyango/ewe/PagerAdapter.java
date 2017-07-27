package com.example.silasonyango.ewe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fraglist = new ArrayList<Fragment>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return fraglist.get(i);
    }

    @Override
    public int getCount() {
        return fraglist.size();
    }

    public void addFragment(Fragment frag) {
        fraglist.add(frag);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
