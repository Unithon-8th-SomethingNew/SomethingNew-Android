package com.unithon.somethingnew.data.network.interceptor

import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.unithon.somethingnew.data.network.base.NetworkCommons.AUTH_HEADER
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor(private val preferenceManager: PreferenceManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest =
            chain.request().newBuilder().addHeader(AUTH_HEADER, preferenceManager.getAccessToken())
                .build()
        return chain.proceed(newRequest)
    }
}