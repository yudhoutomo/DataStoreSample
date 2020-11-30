package com.crocodic.datastore.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("regid") regid: String?
    ): Single<ResponseBody>

}