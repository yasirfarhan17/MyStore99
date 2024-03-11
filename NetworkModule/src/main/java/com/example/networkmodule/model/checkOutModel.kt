package com.example.networkmodule.model

import java.util.*
import kotlin.collections.ArrayList

data class checkOutModel(
    val list:ArrayList<CartModel>?=null,
    val orderId:String?=null,
    val date: String?=null,
    val time:String?=null,
    val currentDate:String?=null,
    val add:String?=null,
    val amount:String?=null,
    val paymentMode:String?=null,
    val phone:String?=null,
    val name:String?=null,

)