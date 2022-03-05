package com.unithon.somethingnew.presentation.main


import android.Manifest
import android.os.Build
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.unithon.somethingnew.R
import com.unithon.somethingnew.databinding.ActivityMainBinding
import com.unithon.somethingnew.presentation.adapter.viewpager.MainViewPagerAdapter
import com.unithon.somethingnew.presentation.base.BaseActivity
import com.unithon.somethingnew.presentation.utility.setStatusBarTransparent

class MainActivity(override val layoutResId: Int = R.layout.activity_main) :
    BaseActivity<ActivityMainBinding>() {
    lateinit var mainViewPagerAdapter: MainViewPagerAdapter
    private val tabIconList = listOf<Int>(R.drawable.ic_home, R.drawable.ic_friends, R.drawable.ic_settings)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewPagerAdapter = MainViewPagerAdapter(this)
        setTheme(R.style.FullScreen)
        setStatusBarTransparent(this, binding.rootView)

        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                // 접근허용 시 실행할 코드

            }

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
            viewPager.adapter = mainViewPagerAdapter
            viewPager.isUserInputEnabled = false // 뷰 페이저 슬라이드 허용 안 함
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.icon = getDrawable(tabIconList[position])
            }.attach() // 탭 클릭시 Fragment 전환
        }

    }

}