package com.example.consumeradda.service

import com.example.consumeradda.models.profileModels.ProfileDefaultResponse
import com.example.consumeradda.models.profileModels.UpdateProfileDefaultResponse
import com.example.consumeradda.models.profileModels.UpdateProfileModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileService {
    @GET("user/profile")
    fun getUserProfile(): Call<ProfileDefaultResponse>

    @PUT("user/profile")
    fun updateProfile(
            @Body updateProfileModel: UpdateProfileModel
    ): Call<UpdateProfileDefaultResponse>
}