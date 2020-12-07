package com.example.consumeradda.models.caseModels

import com.google.gson.annotations.SerializedName

class FilterCaseModel (
    @field:SerializedName("district")
    val district: String? = null,

    @field:SerializedName("state")
    val state: String? = null
)