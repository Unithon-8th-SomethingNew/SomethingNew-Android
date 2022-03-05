package com.unithon.somethingnew.data.network

import com.unithon.somethingnew.data.model.UserModel
import com.unithon.somethingnew.data.network.request.ChangeStreetModel
import com.unithon.somethingnew.data.network.request.KnockModel
import com.unithon.somethingnew.data.network.response.CallableFriendModel
import com.unithon.somethingnew.data.network.response.FriendResponse
import com.unithon.somethingnew.data.network.response.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("/auth/kakao")
    suspend fun loginKakao(
        @Query("token") accessToken: String,
        @Query("fcmToken") fcmToken: String,
        @Query("street") street: String
    ): Response<LoginResponse>

    @POST("/auth/naver")
    suspend fun loginNaver(
        @Query("token") accessToken: String,
        @Query("fcmToken") fcmToken: String,
        @Query("street") street: String
    ): Response<LoginResponse>

    @PUT("/user/callable")
    suspend fun toggleCallable(@Body userModel: UserModel)

    @POST("/friend/request")
    suspend fun requestFriend(
        @Query("fromUserId") userId: Long,
        @Query("toUserEmail") friendEmail: String
    ): Response<Unit>

    @POST("/knock")
    suspend fun sendFcm(@Body knockModel: KnockModel)

    @GET("/friend/location/{userId}")
    suspend fun getCallableFriendList(@Path("userId") uid: Long): Response<List<CallableFriendModel>>

    @GET("/friend/find")
    suspend fun getFriendList(@Query("userId") uid: Long): Response<List<FriendResponse>>

    @PUT("/user/street")
    suspend fun updateStreet(@Body updateBody: ChangeStreetModel): Response<Unit>
}