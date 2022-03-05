package com.unithon.somethingnew.presentation.friends

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.unithon.somethingnew.R
import com.unithon.somethingnew.data.network.MainApi
import com.unithon.somethingnew.data.network.response.FriendResponse
import com.unithon.somethingnew.presentation.call.CallActivity
import kotlinx.android.synthetic.main.dialog_add_frend.cancelBtn
import kotlinx.android.synthetic.main.dialog_add_frend.okBtn
import kotlinx.android.synthetic.main.dialog_call.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
이 TopRecyclerView 는 ViewHolder 의 Binding 을 통해 작성되었습니다.
 */

internal class FriendRecyclerView
constructor(private var arrayList: MutableList<FriendResponse>, private val context: Context) :
    RecyclerView.Adapter<FriendRecyclerView.ViewHolder>() {

    private val preferenceManager = PreferenceManager(context)


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycle_frend_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        Glide.with(context).load(arrayList[position].friendImage).into(viewHolder.friendImage)
        viewHolder.friendName.text = arrayList[position].friendName
        viewHolder.friendAddress.text = arrayList[position].address
        if (arrayList[position].callEnable) {
            viewHolder.callDisable.visibility = View.GONE
            viewHolder.callEnable.visibility = View.VISIBLE
        } else {
            viewHolder.callDisable.visibility = View.VISIBLE
            viewHolder.callEnable.visibility = View.GONE
        }

        viewHolder.rootView.setOnClickListener {
            if (arrayList[viewHolder.adapterPosition].callEnable) {

                Dialog(context).apply {
                    setContentView(R.layout.dialog_call)
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    name_text_view.text = "${arrayList[viewHolder.adapterPosition].friendName} 님에게\n놀러갈까요?"

                    cancelBtn.setOnClickListener {
                        this.dismiss()
                    }
                    okBtn.setOnClickListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            MainApi().sendFcm(
                                preferenceManager.getLong(PreferenceManager.KEY_UID),
                                arrayList[viewHolder.adapterPosition].friendId
                            )
                            context.startActivity(
                                Intent(
                                    context,
                                    CallActivity::class.java
                                ).putExtra(
                                    "channelId",
                                    preferenceManager.getLong(PreferenceManager.KEY_UID).toString()
                                )
                            )
                        }
                    }
                }.show()


            }

        }


    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<FriendResponse>) {
        arrayList.clear()
        arrayList.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        val friendImage: ImageView
        val friendName: TextView
        val friendAddress: TextView
        val callEnable: ImageView
        val callDisable: ImageView
        val rootView: ConstraintLayout

        //ViewHolder
        init {
            friendName = view.findViewById<View>(R.id.friend_name) as TextView
            friendAddress = view.findViewById<View>(R.id.friend_address) as TextView
            friendImage = view.findViewById<View>(R.id.friend_image) as ImageView
            callEnable = view.findViewById(R.id.enable_call) as ImageView
            callDisable = view.findViewById(R.id.disable_call) as ImageView
            rootView = view.findViewById(R.id.root_view) as ConstraintLayout
        }
    }
}