package com.unithon.somethingnew.data.network.response

import com.google.gson.annotations.SerializedName

data class CallableFriendModel(
    @SerializedName("canCall")
    val canCall: Boolean,
    @SerializedName("email")
    val email: String,
    @SerializedName("imgUrl")
    val profileUrl: String,
    @SerializedName("street")
    val street: String,
    @SerializedName("userId")
    val uid: Long,
    @SerializedName("username")
    val username: String,
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double,


    /*@SerializedName("bellCount")
    val bellCount: Int,*/
)
