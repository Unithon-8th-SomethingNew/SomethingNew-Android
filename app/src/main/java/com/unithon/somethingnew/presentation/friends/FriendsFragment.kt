package com.unithon.somethingnew.presentation.friends

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.unithon.somethingnew.R
import com.unithon.somethingnew.data.network.response.FriendResponse
import com.unithon.somethingnew.databinding.FriendsFragmentBinding
import com.unithon.somethingnew.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.friends_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class FriendsFragment(override val layoutResId: Int = R.layout.friends_fragment) :
    BaseFragment<FriendsFragmentBinding>(){

    private val ListArray = ArrayList<FriendResponse>()
    private lateinit var itemAdapter: FriendRecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.recyclerview.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.HORIZONTAL,
            false
        )

        ListArray[0].friendName = "gi"
        ListArray[0].friendAddress = "gigigigigi"

        itemAdapter = FriendRecyclerView(ListArray, requireContext())
        binding.recyclerview.adapter = itemAdapter

    }
}