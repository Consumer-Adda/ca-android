package com.example.consumeradda.models.profileModels

import com.google.gson.annotations.SerializedName

data class ProfileDefaultResponse (

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("firebase_id")
    val firebaseId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: Boolean? = null,

    @field:SerializedName("is_email_verified")
    val isEmailVerified: Boolean? = null,

    @field:SerializedName("profile_image")
    val profileImage: Any? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("location")
    val location: String? = null,

    @field:SerializedName("occupation")
    val occupation: String? = null,

    @field:SerializedName("is_lawyer")
    val isLawyer: String? = null,

    @field:SerializedName("phone_number")
    val phoneNumber: String? = null
)