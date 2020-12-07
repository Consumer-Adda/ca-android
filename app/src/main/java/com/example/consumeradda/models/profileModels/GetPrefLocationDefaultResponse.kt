package com.example.consumeradda.models.profileModels

import com.google.gson.annotations.SerializedName

data class GetPrefLocationDefaultResponse(
        @field:SerializedName("State")
        val state: String? = null,

        @field:SerializedName("District")
        val district: String? = null
)