package com.unithon.somethingnew.presentation.adapter.viewpager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.unithon.somethingnew.presentation.friends.FriendsFragment;
import com.unithon.somethingnew.presentation.main.MapFragment;
import com.unithon.somethingnew.presentation.setting.SettingsFragment;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;


    public ContentsPagerAdapter(FragmentManager fm, int pageCount) {

        super(fm);

        this.mPageCount = pageCount;

    }


    @Override

    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MapFragment mapFragment = new MapFragment();
                return mapFragment;
            case 1:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            case 2:
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return null;
        }
    }

    @Override

    public int getCount() {

        return mPageCount;

    }

}