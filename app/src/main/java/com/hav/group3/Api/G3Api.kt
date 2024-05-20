package com.hav.group3.Api

import com.hav.group3.Model.DataResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface G3Api {

    @FormUrlEncoded
    @POST("api/login-app")
    fun checkLogin(
        @Field("username") userName: String?,
        @Field("password") passWord: String?
    ): Call<DataResponse?>?

}