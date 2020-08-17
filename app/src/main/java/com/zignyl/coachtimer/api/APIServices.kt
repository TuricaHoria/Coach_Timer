package com.zignyl.coachtimer.api

import com.zignyl.coachtimer.models.UserResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface APIServices {

    @GET("?seed=empatica&inc=name,picture&gender=male&results=10&noinfo")
    fun getUsers() : Observable<UserResponse>
}