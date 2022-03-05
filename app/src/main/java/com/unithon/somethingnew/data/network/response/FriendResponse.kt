package com.unithon.somethingnew.data.network.response

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class FriendResponse(
    @SerializedName("imgUrl")
    val friendImage: Bitmap,
    @SerializedName("username")
    var friendName: String,
    @SerializedName("imgUrl")
    var friendAddress: String,
    @SerializedName("userId")
    val friendID: Long,
    @SerializedName("canCall")
    val callEnable: Boolean = true

)
