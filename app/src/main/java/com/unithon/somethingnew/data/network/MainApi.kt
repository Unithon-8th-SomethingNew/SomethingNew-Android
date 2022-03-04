package com.unithon.somethingnew.data.network

import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager.Companion.KEY_USER_NAME
import com.unithon.somethingnew.App
import com.unithon.somethingnew.data.network.base.BaseApi
import com.unithon.somethingnew.data.network.response.FriendResponse
import com.unithon.somethingnew.data.network.response.LocationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainApi : BaseApi() {
    private val iODispatcher = Dispatchers.IO
    private val preferenceManager = PreferenceManager(App.context())

    suspend fun loginKakao(accessToken: String, fcmToken: String, street: String): Boolean = withContext(iODispatcher) {
        val loginApi = retrofit.create(ApiService::class.java)
        val loginResponse = loginApi.loginKakao(accessToken, fcmToken, street)

        if (loginResponse.isSuccessful) {
            val loginModel = loginResponse.body()
            preferenceManager.setString(KEY_USER_NAME, loginModel?.userName)
            true
        } else {
            false
        }
    }

    suspend fun loginNaver(accessToken: String, fcmToken: String, street: String): Boolean = withContext(iODispatcher) {
        val loginApi = retrofit.create(ApiService::class.java)
        val loginResponse = loginApi.loginNaver(accessToken, fcmToken, street)

        if (loginResponse.isSuccessful) {
            val loginModel = loginResponse.body()
            preferenceManager.setString(KEY_USER_NAME, loginModel?.userName)
            true
        } else {
            false
        }
    }

    /*
    suspend fun getFriend(userID: Long): FriendResponse = withContext(iODispatcher) {
        val friendApi = retrofit.create(ApiService::class.java)
        val friendResponse = loginApi.getFriend(userID)

        if (loginResponse.isSuccessful) {
            val loginModel = loginResponse.body()
            preferenceManager.setString(KEY_USER_NAME, loginModel?.userName)
            true
        } else {
            false
        }
    }
     */

}