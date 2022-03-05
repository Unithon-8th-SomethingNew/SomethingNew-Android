package com.unithon.somethingnew.presentation.main


import android.Manifest
import android.os.Build
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.unithon.somethingnew.R
import com.unithon.somethingnew.databinding.ActivityMainBinding
import com.unithon.somethingnew.presentation.adapter.viewpager.ContentsPagerAdapter
import com.unithon.somethingnew.presentation.base.BaseActivity
import com.unithon.somethingnew.presentation.utility.setStatusBarTransparent


class MainActivity(override val layoutResId: Int = R.layout.activity_main) :
    BaseActivity<ActivityMainBinding>() {
    private var waitTime = 0L
    private var mContentPagerAdapter: ContentsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.FullScreen)
        setStatusBarTransparent(this, binding.rootView)


            override fun onPermissionDenied(deniedPermissions: List<String>) {
                // 접근거부 시 실행할 코드
                showToast("권한을 허용해주세요!")
                finish()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("접근 거부하셨습니다.\n[설정] - [권한]에서 권한을 허용해주세요.")
                .setPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    //Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    //Manifest.permission.BLUETOOTH_SCAN
                )
                .check()
        } else {
            TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("접근 거부하셨습니다.\n[설정] - [권한]에서 권한을 허용해주세요.")
                .setPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
                .check()
        }



        with(binding) {


            mContentPagerAdapter = ContentsPagerAdapter(
                supportFragmentManager, tabLayout.tabCount
            )

            viewPager.setAdapter(mContentPagerAdapter)



            viewPager.addOnPageChangeListener(
                TabLayoutOnPageChangeListener(tabLayout)
            )

            tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.setCurrentItem(tab.position)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }

    }

    override fun onBackPressed() {
        if (MapFragment.isReceive.value == false) {
            MapFragment.isReceive.value = true
            return
        }
        if (System.currentTimeMillis() - waitTime >= 1500) {
            waitTime = System.currentTimeMillis()
            showToast("한번 더 누르면 종료되요!")
        } else {
            finish() // 액티비티 종료
        }
    }
}