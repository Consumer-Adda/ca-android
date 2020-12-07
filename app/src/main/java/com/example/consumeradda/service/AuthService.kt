package com.example.consumeradda.service

import com.example.consumeradda.models.authModels.Register
import com.example.consumeradda.models.authModels.RegisterDefaultResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("user/register")
    fun registerUser(
            @Body register: Register
    ): Call<RegisterDefaultResponse>
}