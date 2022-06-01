package com.example.networkmodule.model

import android.os.Parcelable
import com.example.networkmodule.database.entity.CartEntity
import kotlinx.parcelize.Parcelize


@Parcelize
data class CartModel (
    var products_name: String? = null,
    var price: String? = null,
    var img: String? = null,
    var weight: String? = null,
    var quant: String? = null,
    var total: String? = null,
):Parcelable{
    fun toCartEntity(): CartEntity {
        return CartEntity(this.products_name!!,this.price,this.img,this.weight,this.quant,this.total)
    }
}