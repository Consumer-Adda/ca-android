package com.example.consumeradda.models

import com.google.gson.annotations.SerializedName

data class LocationDataModel(
    val states: List<StatesItem?>? = null
)

data class DistrictsItem(
    val district: String? = null,
)

data class StatesItem(
    val districts: List<DistrictsItem?>? = null,
    val state: String? = null
)