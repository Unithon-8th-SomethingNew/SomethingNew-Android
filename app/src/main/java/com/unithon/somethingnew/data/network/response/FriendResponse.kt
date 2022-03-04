package com.unithon.somethingnew.data.network.response

import android.graphics.Bitmap

data class FriendResponse(
    val friendImage: Bitmap,
    val friendName: String,
    val friendAddress: String,
    val fcmToken: String,
    val userID: Long,
    val callEnable: Boolean = true

)
