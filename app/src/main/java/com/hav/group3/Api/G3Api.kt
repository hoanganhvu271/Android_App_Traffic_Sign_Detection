package com.hav.group3.Api

import com.hav.group3.Model.DataResponse
import com.hav.group3.Model.HistoryResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface G3Api {

    @FormUrlEncoded
    @POST("api/login-app")
    fun checkLogin(
        @Field("username") userName: String?,
        @Field("password") passWord: String?
    ): Call<DataResponse?>?

    @GET("api/get-history")
    fun getDetectionHistory(@Query("id") id: String?): Call<HistoryResponse?>?

    @FormUrlEncoded
    @POST("api/new-history")
    fun createNewHistory(
        @Field("SignId") signId: String?,
        @Field("Time") time: String?,
        @Field("UserId") userId: String?
    ): Call<DataResponse?>?
}