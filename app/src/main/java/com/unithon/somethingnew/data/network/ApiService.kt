package com.unithon.somethingnew.data.network

import com.unithon.somethingnew.data.model.UserModel
import com.unithon.somethingnew.data.network.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/kakao")
    suspend fun loginKakao(@Query("token") accessToken: String, @Query("fcmtoken") fcmToken: String, @Query("street") street: String): Response<LoginResponse>

    @POST("/auth/naver")
    suspend fun loginNaver(@Query("token") accessToken: String, @Query("fcmtoken") fcmToken: String, @Query("street") street: String): Response<LoginResponse>

    @PUT("/user/callable")
    suspend fun toggleCallable(@Body userModel: UserModel)
}