package com.example.networkmodule.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import com.example.networkmodule.database.entity.ProductEntity
import kotlinx.parcelize.Parcelize

data class ProductModelNew(
    var products_name: String? = null,
    var price: String? = null,
    var img: String? = null,
    var quant: String? = null,
    var hindiName: String? = null,
    var stock: String? = null
)