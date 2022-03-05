package com.unithon.somethingnew.data.network.request

import com.google.gson.annotations.SerializedName

data class ChangeStreetModel(
    @SerializedName("userId")
    val uid: Long,
    @SerializedName("street")
    val street: String,
)