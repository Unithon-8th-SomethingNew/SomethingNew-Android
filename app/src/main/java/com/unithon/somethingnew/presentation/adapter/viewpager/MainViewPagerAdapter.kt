package com.unithon.somethingnew.presentation.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.unithon.somethingnew.presentation.main.MapFragment

class MainViewPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {

    private val fragmentList = listOf<Fragment>(MapFragment(), MapFragment(), MapFragment())

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

}