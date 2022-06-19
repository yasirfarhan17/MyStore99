package com.example.networkmodule.model


data class User(
    var address:String?=null,
    var name: String?=null,
    var otpVerified: Boolean?=null,
    var password: String?=null,
    val pincode:String?=null,
    var uid: String?=null,
    var image: String?=null

)

