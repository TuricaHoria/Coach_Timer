package com.zignyl.coachtimer.api

import com.zignyl.coachtimer.models.UserResponse
import io.reactivex.Observable

object APIClientRepository {

    private val authenticationAPIPreAuth: APIServices by lazy {
        RetrofitClientInstance.retrofitClientPreAuth.create(APIServices::class.java)
    }

    private val authenticationAPI: APIServices by lazy {
        RetrofitClientInstance.retrofitClient.create(APIServices::class.java)
    }

    fun getAthletes() : Observable<UserResponse> {
        return authenticationAPI.getUsers()
    }

}