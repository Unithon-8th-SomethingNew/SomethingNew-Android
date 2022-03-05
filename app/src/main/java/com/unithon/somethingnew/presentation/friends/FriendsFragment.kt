package com.unithon.somethingnew.presentation.friends

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unithon.somethingnew.R
import com.unithon.somethingnew.data.network.response.FriendResponse
import com.unithon.somethingnew.databinding.FriendsFragmentBinding
import com.unithon.somethingnew.presentation.base.BaseFragment

class FriendsFragment(override val layoutResId: Int = R.layout.friends_fragment) :
    BaseFragment<FriendsFragmentBinding>() {

    private val ListArray = ArrayList<FriendResponse>()
    private lateinit var itemAdapter: FriendRecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
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
}