package com.unithon.somethingnew.data.network

import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager.Companion.KEY_USER_NAME
import com.unithon.somethingnew.App
import com.unithon.somethingnew.data.network.base.BaseApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainApi : BaseApi() {
    private val iODispatcher = Dispatchers.IO
    private val preferenceManager = PreferenceManager(App.instance)

    suspend fun login(accessToken: String): Boolean = withContext(iODispatcher) {
        val loginApi = retrofit.create(ApiService::class.java)
        val loginResponse = loginApi.login(accessToken)

        if (loginResponse.isSuccessful) {
            val loginModel = loginResponse.body()
            preferenceManager.setString(KEY_USER_NAME, loginModel?.userName)
            true
        } else {
            false
        }
    }
}