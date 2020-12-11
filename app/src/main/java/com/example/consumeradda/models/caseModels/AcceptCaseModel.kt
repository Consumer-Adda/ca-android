package com.example.consumeradda.models.caseModels

import com.google.gson.annotations.SerializedName

class AcceptCaseModel(
    @field:SerializedName("application_id")
    val applicationId: Int ?= null
)