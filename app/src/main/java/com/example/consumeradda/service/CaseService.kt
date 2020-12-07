package com.example.consumeradda.service

import com.example.consumeradda.models.caseModels.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CaseService {
    @POST("application/submit")
    fun submitApplication(
        @Body application: SubmitCaseModel
    ): Call<SubmitCaseDefaultResponse>

    @GET("application/")
    fun getApplications(): Call<MutableList<CaseResponse>>

    @POST("application/accept")
    fun acceptApplication(
        @Body application: AcceptCaseModel
    ): Call<AcceptCaseDefaultReponse>

    @POST("application/filter")
    fun getFilteredApplications(
        @Body filter: FilterCaseModel
    ): Call<MutableList<CaseResponse>>
}