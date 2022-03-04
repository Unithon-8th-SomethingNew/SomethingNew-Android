package com.unithon.somethingnew.presentation.main


import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.unithon.somethingnew.R
import com.unithon.somethingnew.databinding.ActivityMainBinding
import com.unithon.somethingnew.presentation.base.BaseActivity

class MainActivity(override val layoutResId: Int = R.layout.activity_main) :
    BaseActivity<ActivityMainBinding>(), OnMapReadyCallback {

    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


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

        }

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(p0: NaverMap) {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                /*
                Log.d("Test", "GPS Location Latitude: $latitude" +
                        ", Longitude: $longitude")

                 */

                val marker = Marker()
                marker.position = LatLng(latitude, longitude)
                marker.icon = OverlayImage.fromResource(R.drawable.maker)
                marker.width = 100
                marker.height = 120
                marker.map = p0
            }
        }

    }

}