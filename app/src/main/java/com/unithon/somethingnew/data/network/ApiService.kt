package com.unithon.somethingnew.data.network

import com.unithon.somethingnew.data.network.response.LoginResponse
import retrofit2.Response
import retrofit2.http.POST

interface ApiService {
    @POST("/login/oauth2")
    fun login(): Response<LoginResponse>
}