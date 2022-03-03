package com.unithon.somethingnew.di.modules

import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.unithon.somethingnew.data.network.base.buildOkHttpClientWithAccessToken
import com.unithon.somethingnew.data.network.base.provideGsonConverterFactory
import com.unithon.somethingnew.data.network.base.provideRetrofit
import com.unithon.somethingnew.data.network.interceptor.HttpLogInterceptorProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModules = module {
    single { provideGsonConverterFactory() }

    single {
        buildOkHttpClientWithAccessToken(
            get<HttpLogInterceptorProvider>().getInterceptor()
        )
    }

    single { provideRetrofit(get(), get()) }
    single { HttpLogInterceptorProvider() }

    //SharedPreference
    single { PreferenceManager(androidContext()) }
    /* API Modules
    * */

}
