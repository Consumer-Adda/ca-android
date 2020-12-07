package com.example.consumeradda.models.profileModels

import com.google.gson.annotations.SerializedName

data class PrefLocationModel (
        @field:SerializedName("state")
        val state: String? = "",

        @field:SerializedName("district")
        val district: String? = ""
)