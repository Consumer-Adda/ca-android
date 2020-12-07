package com.example.consumeradda.service

import com.example.consumeradda.models.DashboardDefaultResponse
import com.example.consumeradda.models.profileModels.GetPrefLocationDefaultResponse
import com.example.consumeradda.models.profileModels.PrefLocationModel
import com.example.consumeradda.models.profileModels.UpdateLocationDefaultResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DashboardService {
    @GET("user/dashboard")
    fun getUserRole(): Call<DashboardDefaultResponse>

    @POST("user/preferredlocation")
    fun updateLocation(
            @Body prefLocationModel: PrefLocationModel
    ): Call<UpdateLocationDefaultResponse>

    @GET("user/preferredlocation")
    fun getPrefLocation(): Call<GetPrefLocationDefaultResponse>
}