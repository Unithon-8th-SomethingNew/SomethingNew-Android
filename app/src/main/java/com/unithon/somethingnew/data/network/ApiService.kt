package com.unithon.somethingnew.data.network

import com.unithon.somethingnew.data.network.response.LoginResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/kakao")
    suspend fun loginKakao(@Query("token") accessToken: String, @Query("fcmtoken") fcmToken: String, @Query("street") street: String): Response<LoginResponse>

    @POST("/auth/naver")
    suspend fun loginNaver(@Query("token") accessToken: String, @Query("fcmtoken") fcmToken: String, @Query("street") street: String): Response<LoginResponse>

    /*
    @post("/auth/naver")
    suspend fun getFriend(): Response<FriendResponse>
     */

}