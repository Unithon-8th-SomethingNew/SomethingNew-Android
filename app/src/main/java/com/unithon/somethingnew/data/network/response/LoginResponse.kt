package com.unithon.somethingnew.data.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("userId")
    val uid: Long,
    @SerializedName("username")
    val userName: String,
    @SerializedName("imgUrl")
    val profileUrl: String? = null
)