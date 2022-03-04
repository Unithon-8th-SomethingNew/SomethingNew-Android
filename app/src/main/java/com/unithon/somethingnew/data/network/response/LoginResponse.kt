package com.unithon.somethingnew.data.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("uid")
    val uid: Long,
    @SerializedName("name")
    val userName: String,
    @SerializedName("profile_url")
    val profileUrl: String

)