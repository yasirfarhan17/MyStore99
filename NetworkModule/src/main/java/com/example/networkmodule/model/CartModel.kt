package com.example.networkmodule.model

import android.os.Parcelable
import com.example.networkmodule.database.entity.CartEntity
import kotlinx.parcelize.Parcelize


@Parcelize
data class CartModel (
    var products_name: String? = null,
    var price: String? = null,
    var img: String? = null,
    var quant: String? = null,
    var total: String? = null,
    var count:Int=0
):Parcelable{
    fun toCartEntity(): CartEntity {
        return CartEntity(this.products_name!!,this.price,this.img,this.quant,this.total,this.count)
    }
}