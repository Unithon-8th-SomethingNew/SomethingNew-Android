package com.unithon.somethingnew.presentation.friends

import android.graphics.Bitmap


class AdapterItem {

    lateinit var friendImage: Bitmap
    lateinit var friendName: String
    lateinit var friendAddress: String
    lateinit var fcmToken: String
    var userID: Long = 0L
    var callEnable: Boolean = true


}