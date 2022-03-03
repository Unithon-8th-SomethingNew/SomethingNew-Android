package com.unithon.somethingnew.data.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("username")
    val userName: String
)