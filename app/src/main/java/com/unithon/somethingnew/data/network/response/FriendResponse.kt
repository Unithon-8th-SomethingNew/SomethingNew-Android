package com.unithon.somethingnew.data.network.response

import android.graphics.Bitmap

data class FriendResponse(
    val friendImage: Bitmap,
    var friendName: String,
    var friendAddress: String,
    val fcmToken: String,
    val friendID: Long,
    val callEnable: Boolean = true

)
