package com.unithon.somethingnew.data.network.response

import com.google.gson.annotations.SerializedName

data class CallableFriendModel(
    @SerializedName("uid")
    val uid: Long,
    @SerializedName("username")
    val username: String,
    @SerializedName("imgUrl")
    val profileUrl: String,
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double,
    @SerializedName("fcmToken")
    val fcmToken: String,
    /*@SerializedName("bellCount")
    val bellCount: Int,*/
)
