package com.example.networkmodule.model

data class UserModel(
    val address: String? = null,
    val name:String?=null,
    val otpVerified:Boolean=false,
    val password:String?=null,
    val pincode:String?=null,
    val uid:String?=null

)