package com.zignyl.coachtimer.api

import cst.baseappsetup.ApplicationController
import cst.baseappsetup.data.client.APIClient
import cst.baseappsetup.data.network.authenticators.SessionAuthenticator
import cst.baseappsetup.data.network.interceptors.SessionAuthInterceptor
import cst.baseappsetup.data.network.interceptors.SessionPreAuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Boolean
import java.util.concurrent.TimeUnit

object RetrofitClientInstance : APIClient() {

    private val preAuthInterceptor by lazy {
        SessionPreAuthInterceptor(context = ApplicationController.instance)
    }

    private val authInterceptor by lazy {
        SessionAuthInterceptor(context = ApplicationController.instance)
    }

    private val authenticator by lazy {
        SessionAuthenticator
    }

    val retrofitClientPreAuth: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(getBaseApi())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientPreAuth)
            .build()
    }


    private val okHttpClientPreAuth: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(preAuthInterceptor)
            .addInterceptor(networkInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val retrofitClient: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(getBaseApi())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .followRedirects(Boolean.FALSE)
            .followSslRedirects(Boolean.FALSE)
            .addInterceptor(authInterceptor)
            .addInterceptor(networkInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}