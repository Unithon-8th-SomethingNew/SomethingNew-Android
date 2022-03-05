package com.unithon.somethingnew.data.network.response

import com.google.gson.annotations.SerializedName

data class CallableFriendModel(
    @SerializedName("uid")
    val uid: Long,
    @SerializedName("username")
    val username: String,
    @SerializedName("profile_url")
    val profileUrl: String,
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double,
    @SerializedName("fcmtoken")
    val fcmToken: String,
)
