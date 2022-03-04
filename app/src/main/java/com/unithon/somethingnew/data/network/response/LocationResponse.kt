package com.unithon.somethingnew.data.network.response

import com.google.gson.annotations.SerializedName

data class LocationResponse (
    @SerializedName("location")
    val userName: String,
    val latitude: Double,
    val longitude: Double
)