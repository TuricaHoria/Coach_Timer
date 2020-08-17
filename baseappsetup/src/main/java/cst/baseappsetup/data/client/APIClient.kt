package cst.baseappsetup.data.client

import cst.baseappsetup.BuildConfig
import cst.baseappsetup.ApplicationController
import cst.baseappsetup.data.network.interceptors.NetworkInterceptor
import okhttp3.logging.HttpLoggingInterceptor

open class APIClient {

    companion object {
        const val AUTHORIZATION: String = "Authorization"
        const val BEARER = "Bearer"
        const val BASIC = "Basic"
        const val BASIC_AUTH_VALUE =  "https://randomuser.me/api/"

        const val ON_START_DELAY: Long = 200

        const val CHANNEL_ID = "channelId"
    }

    protected val networkInterceptor by lazy {
        NetworkInterceptor(context = ApplicationController.instance)
    }

    protected val loggingInterceptor: HttpLoggingInterceptor by lazy {
        if(BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        } else {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    protected fun getBaseApi(): String =  "https://randomuser.me/api/"

}