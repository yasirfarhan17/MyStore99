package com.example.networkmodule.model

import android.os.Parcelable
import com.example.networkmodule.database.entity.ProductEntity
import kotlinx.parcelize.Parcelize


@Parcelize
data class ProductModel(
    var products_name: String? = null,
    var price: String? = null,
    var img: String? = null,
    var quant: String? = null,
    var hindiName: String? = null,
    var stock: String? = null,
    var count: Int = 0
) : Parcelable {
    fun toProductEntity(): ProductEntity {
        return ProductEntity(
            this.products_name!!,
            this.price,
            this.img,
            this.quant,
            this.hindiName,
            this.stock,
            this.count
        )
    }
}