package com.unithon.somethingnew.data.network.request

import com.google.gson.annotations.SerializedName

data class KnockModel(
    @SerializedName("fromUserId")
    val myUid: Long,
    @SerializedName("toUserId")
    val otherUid: Long
)
