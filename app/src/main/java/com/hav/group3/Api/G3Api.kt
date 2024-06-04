package com.hav.group3.Api

import com.hav.group3.Model.DataResponse
import com.hav.group3.Model.HistoryResponse
import com.hav.group3.Model.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface G3Api {



    @FormUrlEncoded
    @POST("api/login-app")
    fun checkLogin(
        @Field("username") userName: String?,
        @Field("password") passWord: String?
    ): Call<LoginResponse?>?

    @GET("api/get-history")
    fun getDetectionHistory(): Call<HistoryResponse?>?

    @FormUrlEncoded
    @POST("api/new-history")
    fun createNewHistory(
        @Field("SignId") signId: String?,
        @Field("Time") time: String?,
        @Field("Longitude") longitude: String?,
        @Field("Latitude") latitude: String?
    ): Call<DataResponse?>?

    @FormUrlEncoded
    @POST("api/register")
    fun createNewAccount(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("name") name : String?,
        @Field("email") email: String?,
        @Field("phone") phone: String?
    ): Call<DataResponse?>?

    @Multipart
    @POST("api/send-feedback")
    fun sendFeedback(
        @Part image: MultipartBody.Part,
        @Part("text") content: String?
    ): Call<DataResponse>

    @FormUrlEncoded
    @POST("api/send-otp")
    fun sendOtp(
        @Field("email") email: String?
    ): Call<DataResponse?>?

    @FormUrlEncoded
    @POST("api/verify-otp")
    fun verifyOtp(
        @Field("email") email: String?,
        @Field("otp") otp: String?
    ): Call<DataResponse?>?

    @FormUrlEncoded
    @POST("api/change-password")
    fun changePassword(
        @Field("email") email: String?,
        @Field("newPassword") newPassword: String?,
        @Field("confirmPassword") confirmPassword: String?
    ): Call<DataResponse?>?
}