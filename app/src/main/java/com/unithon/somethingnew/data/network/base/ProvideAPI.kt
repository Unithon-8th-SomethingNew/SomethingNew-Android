package com.unithon.somethingnew.data.network.base

import com.unithon.somethingnew.data.network.base.NetworkCommons.TIME_OUT
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/*
 레트로핏 빌더, 인스턴스 관련 함수들 모음 파일
 */

internal fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(NetworkCommons.BASE_URL)
        .addConverterFactory(nullOnEmptyConverterFactory)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

internal fun provideGsonConverterFactory(): GsonConverterFactory {
    return GsonConverterFactory.create()
}

internal val nullOnEmptyConverterFactory = object : Converter.Factory() {
    fun converterFactory() = this
    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
        val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
        override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
    }
}

// Access Token 인터셉터를 포함한 OkHttpClient
internal fun buildOkHttpClientWithAccessToken(
    httpLogInterceptor: HttpLoggingInterceptor,
): OkHttpClient {

    val builder = OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .callTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(httpLogInterceptor) // Http 통신 Log를 출력하기 위한 Interceptor



    return builder.build()
}
