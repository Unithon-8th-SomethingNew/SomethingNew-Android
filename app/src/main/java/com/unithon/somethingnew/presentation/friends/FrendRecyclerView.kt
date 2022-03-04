package com.unithon.somethingnew.presentation.friends

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.unithon.somethingnew.R
import com.unithon.somethingnew.data.network.MainApi
import com.unithon.somethingnew.data.network.response.FriendResponse
import com.unithon.somethingnew.presentation.call.CallActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
이 TopRecyclerView 는 ViewHolder 의 Binding 을 통해 작성되었습니다.
 */

internal class FrendRecyclerView
constructor(private val arrayList: ArrayList<FriendResponse>, private val context: Context) :
    RecyclerView.Adapter<FrendRecyclerView.ViewHolder>() {

    private val preferenceManager = PreferenceManager(context)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycle_frend_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.friendImage.setImageBitmap(arrayList[position].friendImage)
        viewHolder.friendName.text = arrayList[position].friendName
        viewHolder.friendAddress.text = arrayList[position].friendAddress
        if (arrayList[position].callEnable) {
            viewHolder.callEnable.visibility = View.VISIBLE
        } else {
            viewHolder.callEnable.visibility = View.INVISIBLE
        }

        with(viewHolder) {
            viewHolder.friendName.setOnClickListener {
                if (arrayList[viewHolder.adapterPosition].callEnable) {
                    CoroutineScope(Dispatchers.IO).launch {
                        MainApi().sendFcm(
                            preferenceManager.getLong(PreferenceManager.KEY_UID),
                            arrayList[viewHolder.adapterPosition].friendID
                        )
                        friendImage.context.startActivity(
                            Intent(
                                friendImage.context,
                                CallActivity::class.java
                            ).putExtra(
                                "channelId",
                                preferenceManager.getLong(PreferenceManager.KEY_UID).toString()
                            )
                        )
                    }
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        val friendImage: ImageView
        val friendName: TextView
        val friendAddress: TextView
        val callEnable: ImageView


        //ViewHolder
        init {
            friendName = view.findViewById<View>(R.id.friend_name) as TextView
            friendAddress = view.findViewById<View>(R.id.friend_address) as TextView
            friendImage = view.findViewById<View>(R.id.friend_image) as ImageView
            callEnable = view.findViewById(R.id.enable_call) as ImageView
        }
    }
}