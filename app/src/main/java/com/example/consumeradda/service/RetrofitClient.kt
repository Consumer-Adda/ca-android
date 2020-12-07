package com.example.consumeradda.service

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    val authService: AuthService
    val dashboardService:DashboardService
    val caseService:CaseService
    val chatService:ChatService
    var idToken=""

    companion object {
        private var retrofitClient: RetrofitClient? = null

        val instance: RetrofitClient
            get() {
                if (retrofitClient == null) {
                    retrofitClient = RetrofitClient()
                }
                return retrofitClient as RetrofitClient
            }
    }


    init
    {

        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()

            var urlString=original.url.toString()
            urlString=urlString.replace("%20"," ")

            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", idToken)
                .method(original.method, original.body).url(urlString)

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://consumer-adda.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val retrofitWithHeader = Retrofit.Builder().client(okHttpClient)
            .baseUrl("https://consumer-adda.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        authService=retrofit.create(AuthService::class.java)
        dashboardService=retrofitWithHeader.create(DashboardService::class.java)
        caseService=retrofitWithHeader.create(CaseService::class.java)
        chatService=retrofitWithHeader.create(ChatService::class.java)
    }

}