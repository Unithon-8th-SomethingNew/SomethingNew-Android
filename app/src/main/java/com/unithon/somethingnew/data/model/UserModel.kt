package com.unithon.somethingnew.data.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("userId")
    var userId: Long,
    @SerializedName("canCall")
    var isCallable: Boolean
)