package com.example.networkmodule.database.product

import android.os.Parcelable
import com.example.networkmodule.database.product.ProductEntity
import kotlinx.parcelize.Parcelize


@Parcelize
data class productModel (
    var products_name: String? = null,
    var price: String? = null,
    var img: String? = null,
    var quant: String? = null,
    var hindiName: String? = null,
    var stock: String? = null
):Parcelable{
    fun toProductEntity(): ProductEntity {
        return ProductEntity(this.products_name!!,this.price,this.img,this.quant,this.hindiName,this.stock)
    }
}