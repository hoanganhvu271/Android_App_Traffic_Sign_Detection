package com.hav.group3.Api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://backend-1-rnqj.onrender.com"

    fun createClient(token: String): G3Api {
        val authInterceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("access-token", "$token")
                .build()
            chain.proceed(newRequest)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(G3Api::class.java)
    }
}