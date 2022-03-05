package com.unithon.somethingnew.data.network

import android.util.Log
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager.Companion.KEY_PROFILE_URL
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager.Companion.KEY_UID
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager.Companion.KEY_USER_NAME
import com.unithon.somethingnew.App
import com.unithon.somethingnew.data.model.UserModel
import com.unithon.somethingnew.data.network.base.BaseApi
import com.unithon.somethingnew.data.network.request.ChangeStreetModel
import com.unithon.somethingnew.data.network.request.KnockModel
import com.unithon.somethingnew.data.network.response.CallableFriendModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainApi : BaseApi() {
    private val iODispatcher = Dispatchers.IO
    private val preferenceManager = PreferenceManager(App.instance!!)

    suspend fun loginKakao(accessToken: String, fcmToken: String, street: String): Boolean =
        withContext(iODispatcher) {
            val loginApi = retrofit.create(ApiService::class.java)
            val loginResponse = loginApi.loginKakao(accessToken, fcmToken, street)

            if (loginResponse.isSuccessful) {
                val loginModel = loginResponse.body()
                preferenceManager.setLong(KEY_UID, loginModel?.uid!!)
                preferenceManager.setString(KEY_USER_NAME, loginModel.userName)
                preferenceManager.setString(KEY_PROFILE_URL, loginModel.profileUrl)
                true
            } else {
                false
            }
        }

    suspend fun loginNaver(accessToken: String, fcmToken: String, street: String): Boolean =
        withContext(iODispatcher) {
            val loginApi = retrofit.create(ApiService::class.java)
            val loginResponse = loginApi.loginNaver(accessToken, fcmToken, street)

            if (loginResponse.isSuccessful) {
                val loginModel = loginResponse.body()
                preferenceManager.setLong(KEY_UID, loginModel?.uid!!)
                preferenceManager.setString(KEY_USER_NAME, loginModel.userName)
                preferenceManager.setString(KEY_PROFILE_URL, loginModel.profileUrl)
                true
            } else {
                false
            }
        }

    suspend fun setCall(uid: Long, isCallable: Boolean) = withContext(iODispatcher) {
        val settingApi = retrofit.create(ApiService::class.java)
        val isSuccess = settingApi.toggleCallable(
            UserModel(
                uid, isCallable
            )
        )

        isSuccess
    }

    suspend fun sendFcm(myUid: Long, otherUid: Long) = withContext(iODispatcher) {
        val fcmApi = retrofit.create(ApiService::class.java)
        fcmApi.sendFcm(KnockModel(myUid, otherUid))
        true
    }

    suspend fun getFriendList(myUid: Long) = withContext(iODispatcher) {
        val friendRequestApi = retrofit.create(ApiService::class.java)
        friendRequestApi.getFriendList(myUid).body()
    }

    suspend fun getCallableFriendList(myUid: Long): List<CallableFriendModel>? = withContext(iODispatcher) {
        val friendRequestApi = retrofit.create(ApiService::class.java)
        friendRequestApi.getCallableFriendList(myUid).body()
    }

    suspend fun requestFriend(myUid: Long, friendEmail: String): Boolean = withContext(iODispatcher) {
        val friendRequestApi = retrofit.create(ApiService::class.java)
        val result = friendRequestApi.requestFriend(myUid, friendEmail)
        result.isSuccessful
    }

    suspend fun updateStreet(uid: Long, street: String) = withContext(iODispatcher) {
        val userApi = retrofit.create(ApiService::class.java)
        userApi.updateStreet(ChangeStreetModel(
            uid, street
        ))
    }

    suspend fun bellPushToRun(uid: Long, fuid: Long) = withContext(iODispatcher) {
        val bellApi = retrofit.create(ApiService::class.java)
        bellApi.bellPushToRun(uid, fuid)
    }
}