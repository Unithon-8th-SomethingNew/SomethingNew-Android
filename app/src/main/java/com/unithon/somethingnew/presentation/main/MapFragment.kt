package com.unithon.somethingnew.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.unithon.somethingnew.R
import com.unithon.somethingnew.databinding.FragmentMapBinding
import com.unithon.somethingnew.presentation.base.BaseFragment

class MapFragment(override val layoutResId: Int = R.layout.fragment_map) : BaseFragment<FragmentMapBinding>(),
    OnMapReadyCallback {
    lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val fm = parentFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(p0: NaverMap) {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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