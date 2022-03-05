package com.unithon.somethingnew.data.network.response

import com.google.gson.annotations.SerializedName

data class FriendResponse(
    @SerializedName("imgUrl")
    val friendImage: String,
    @SerializedName("username")
    var friendName: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("street")
    var address: String,
    @SerializedName("userId")
    val friendId: Long,
    @SerializedName("canCall")
    val callEnable: Boolean = true

)
