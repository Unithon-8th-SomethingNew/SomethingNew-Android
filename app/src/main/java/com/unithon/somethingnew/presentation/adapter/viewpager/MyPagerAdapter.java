package com.unithon.somethingnew.presentation.adapter.viewpager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.unithon.somethingnew.presentation.friends.FriendsFragment;
import com.unithon.somethingnew.presentation.main.MapFragment;
import com.unithon.somethingnew.presentation.setting.SettingsFragment;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    // Fragment 페이저 생성자 - 인자로 Fragment Manager를 넘겨야 한다.
    private ArrayList fragments = new ArrayList<Fragment>();

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new MapFragment());
        fragments.add(new FriendsFragment());
        fragments.add(new SettingsFragment());
    }

    // ListView 의 getView 와 같은 역할 - 각 position에 맞는 fragment 를 return해야한다.
    @Override
    public Fragment getItem(int position) {
        return (Fragment) fragments.get(position);
    }

    // fragment 화면 개수
    @Override
    public int getCount() {
        return 3;
    }
}