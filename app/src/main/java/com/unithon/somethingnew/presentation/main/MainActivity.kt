package com.unithon.somethingnew.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Transformations.map
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
