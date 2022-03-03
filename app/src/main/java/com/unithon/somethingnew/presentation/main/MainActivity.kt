package com.unithon.somethingnew.presentation.main

import android.os.Bundle
import com.unithon.somethingnew.R
import com.unithon.somethingnew.databinding.ActivityMainBinding
import com.unithon.somethingnew.presentation.base.BaseActivity

class MainActivity(override val layoutResId: Int = R.layout.activity_main) :
    BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {

        }
    }
}