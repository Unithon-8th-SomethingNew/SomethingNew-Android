package com.unithon.somethingnew.presentation.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager.Companion.KEY_PROFILE_URL
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager.Companion.KEY_USER_NAME
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.unithon.somethingnew.R
import com.unithon.somethingnew.data.network.MainApi
import com.unithon.somethingnew.data.network.response.CallableFriendModel
import com.unithon.somethingnew.databinding.FragmentMapBinding
import com.unithon.somethingnew.databinding.MakerBinding
import com.unithon.somethingnew.presentation.base.BaseFragment
import com.unithon.somethingnew.presentation.havenoke.HaveNokeActivity
import com.unithon.somethingnew.presentation.utility.CustomToast
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext


class MapFragment(override val layoutResId: Int = R.layout.fragment_map) :
    BaseFragment<FragmentMapBinding>(),
    OnMapReadyCallback, CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var preferenceManager: PreferenceManager
    private val callableFriendList = MutableLiveData<List<CallableFriendModel>?>() // ????????? ?????? ????????? ?????????
    private var activeMarkers: Vector<Marker>? = null

    private var naverMap: NaverMap? = null
    private var isFirst = true

    companion object {
        var isReceive = MutableLiveData(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        preferenceManager = PreferenceManager(requireContext())

        launch(Dispatchers.IO) {
            while (true) {
                if (isReceive.value!!) {
                    val friendList = MainApi().getCallableFriendList(
                        preferenceManager.getLong(
                            PreferenceManager.KEY_UID
                        )
                    )
                    callableFriendList.postValue(friendList)
                }
                delay(10000) // 30?????? ????????? ?????????
            }
        }

        callableFriendList.observe(this) { friendList ->
            if (isFirst) {
                isFirst = false
                val spannable = SpannableStringBuilder("?????? ${friendList?.size}?????? ?????????\n????????? ????????????")
                spannable.setSpan(
                    ForegroundColorSpan(Color.rgb(240, 89, 144)),
                    3,
                    friendList?.size.toString().length + 5,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )

                CustomToast.createToast(requireContext(), spannable.toString())?.show()
            }


            freeActiveMarkers() // ?????? ?????? ??????

            friendList?.forEach { friend ->
                val markerBinding = MakerBinding.inflate(layoutInflater)

                markerBinding.makerUsername.text = friend.username
                Glide.with(requireContext()).load(friend.profileUrl)
                    .into(markerBinding.profileImageView)

                val name = friend.username
                val profileUrl = friend.profileUrl
                val uid = friend.uid

                val marker = Marker()
                marker.position = LatLng(friend.y, friend.x)
                marker.icon = OverlayImage.fromView(markerBinding.root)
                marker.width = 120
                marker.height = 180
                marker.map = naverMap
                marker.setOnClickListener {
                    isReceive.value = false

                    //????????????
                    val cameraUpdate = CameraUpdate.scrollTo(
                        LatLng(
                            friend.y, friend.x
                        )
                    )
                    naverMap?.moveCamera(cameraUpdate)

                    isReceive.observe(this) { isReceive ->
                        if (isReceive) {
                            binding.nokeBtn.visibility = View.GONE
                            binding.bellBtn.visibility = View.GONE
                        } else {
                            binding.nokeBtn.visibility = View.VISIBLE
                            binding.bellBtn.visibility = View.VISIBLE
                        }

                    }

                    binding.bellBtn.setOnClickListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            /*val numPushBell: Int = */
                            MainApi().bellPushToRun(preferenceManager.getLong(PreferenceManager.KEY_UID), uid)

                            /*
                            //if (!numPushBell == -1) {

                                val spannable = SpannableStringBuilder("????????? ??????" ${numPushBell}??? "????????????")
                                    spannable.setSpan(
                                    ForegroundColorSpan(Color.rgb(240, 89, 144)),
                                    3,
                                    friendList?.size.toString().length + 5,
                                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                                    )

                                CustomToast.createToast(requireContext(), spannable.toString())?.show()
                            }
                            */
                        }

                    }

                    binding.nokeBtn.setOnClickListener {

                        CoroutineScope(Dispatchers.IO).launch {
                            MainApi().sendFcm(
                                preferenceManager.getLong(PreferenceManager.KEY_UID),
                                uid
                            )
                            startActivity(
                                Intent(
                                    context,
                                    HaveNokeActivity::class.java
                                ).putExtra(
                                    "channelId",
                                    preferenceManager.getLong(PreferenceManager.KEY_UID).toString()
                                ).putExtra(
                                    "name",
                                    name
                                ).putExtra(
                                    "profileUrl",
                                    profileUrl
                                )
                            )
                        }
                    }
                    true
                }
                activeMarkers?.add(marker)
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val fm = parentFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)


        isReceive.observe(this) { isReceive ->
            if (isReceive) {
                binding.nokeBtn.visibility = View.GONE
                binding.bellBtn.visibility = View.GONE
            } else {
                binding.nokeBtn.visibility = View.VISIBLE
                binding.bellBtn.visibility = View.VISIBLE
            }
        }


    }


    override fun onMapReady(naverMap: NaverMap) {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }


        this.naverMap = naverMap
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                val cameraUpdate = CameraUpdate.scrollTo(
                    LatLng(
                        latitude,
                        longitude
                    )
                )
                naverMap.moveCamera(cameraUpdate)

                val marker = Marker()

                CoroutineScope(Dispatchers.Main).launch {
                    val markerBinding = MakerBinding.inflate(layoutInflater)
                    markerBinding.makerUsername.text = preferenceManager.getString(KEY_USER_NAME)
                    Glide.with(requireContext())
                        .load(Uri.parse(preferenceManager.getString(KEY_PROFILE_URL)))
                        .into(markerBinding.profileImageView)

                    delay(1000)

                    marker.position = LatLng(latitude, longitude)
                    marker.icon = OverlayImage.fromView(markerBinding.root)
                    marker.width = 120
                    marker.height = 180
                    marker.map = naverMap
                }
            }

        }
    }

    // ???????????? ?????????????????? ????????? ???????????? ??????
    private fun freeActiveMarkers() {
        if (activeMarkers == null) {
            activeMarkers = Vector()
            return
        }
        for (activeMarker in activeMarkers!!) {
            activeMarker.map = null
        }
        activeMarkers = Vector()
    }

}