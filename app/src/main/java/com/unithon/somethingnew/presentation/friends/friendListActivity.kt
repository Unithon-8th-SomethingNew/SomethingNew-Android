package com.unithon.somethingnew.presentation.friends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.naver.maps.map.OnMapReadyCallback
import com.unithon.somethingnew.App
import com.unithon.somethingnew.R
import com.unithon.somethingnew.data.network.MainApi
import com.unithon.somethingnew.data.network.response.FriendResponse
import com.unithon.somethingnew.databinding.ActivityFriendListBinding
import com.unithon.somethingnew.databinding.ActivityLoginBinding
import com.unithon.somethingnew.databinding.ActivityMainBinding
import com.unithon.somethingnew.presentation.base.BaseActivity
import com.unithon.somethingnew.presentation.base.BaseFragment
import com.unithon.somethingnew.presentation.login.LocationActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class friendListActivity (override val layoutResId: Int = R.layout.activity_friend_list) :
    BaseFragment<ActivityFriendListBinding>() {

    private val ListArray = ArrayList<FriendResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            binding.recyclerview.layoutManager = LinearLayoutManager(
                App.context(), RecyclerView.HORIZONTAL,
                false
            )

            ListArray[0].friendName = "gi"
            ListArray[0].friendAddress = "gigigigigi"

            val ItemAdapter = FrendRecyclerView(ListArray)
            binding.recyclerview.adapter = ItemAdapter

        }
    }
}