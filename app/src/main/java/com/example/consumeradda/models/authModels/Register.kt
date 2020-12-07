package com.example.consumeradda.models.authModels

data class Register (
    val name:String,
    val email:String,
    val password:String,
    val role:Int,
    val phoneNumber:String
)
