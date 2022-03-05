package com.unithon.somethingnew.presentation.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.unithon.somethingnew.App
import com.unithon.somethingnew.R
import com.unithon.somethingnew.data.network.MainApi
import com.unithon.somethingnew.data.network.response.CallableFriendModel
import com.unithon.somethingnew.databinding.FragmentMapBinding
import com.unithon.somethingnew.databinding.MakerBinding
import com.unithon.somethingnew.presentation.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MapFragment(override val layoutResId: Int = R.layout.fragment_map) : BaseFragment<FragmentMapBinding>(),
    OnMapReadyCallback, CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var preferenceManager: PreferenceManager
    private val callableFriendList = MutableLiveData<List<CallableFriendModel>?>() // 사용자 정보 라이브 데이터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        preferenceManager = PreferenceManager(requireContext())

        launch(Dispatchers.IO) {
            val friendList = MainApi().getCallableFriendList(preferenceManager.getLong(
                PreferenceManager.KEY_UID))
            callableFriendList.postValue(friendList)
            Log.d("ddddd", friendList.toString())
        }

        callableFriendList.observe(this) {

        }
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
                val markerBinding = MakerBinding.inflate(layoutInflater)
                markerBinding.makerUsername.text = "이름"
                //markerBinding.makerUserimage.setImageBitmap()


                val marker = Marker()
                marker.position = LatLng(latitude, longitude)
                marker.icon = OverlayImage.fromView(markerBinding.root)
                marker.width = 120
                marker.height = 180
                marker.map = p0
            }
        }

    }
}