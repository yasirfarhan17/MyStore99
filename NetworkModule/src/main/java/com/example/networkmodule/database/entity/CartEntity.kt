package com.example.networkmodule.database.entity

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.networkmodule.model.CartModel
import com.example.networkmodule.model.ProductModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cartTable")
data class CartEntity(
    @PrimaryKey @ColumnInfo val products_name: String="",
    @ColumnInfo val price: String? = null,
    @ColumnInfo val img: String? = null,
    @ColumnInfo val weight: String? = null,
    @ColumnInfo var quant: String? = null,
    @ColumnInfo var total: String? = null,
) : Parcelable {
    fun toProductModel(): CartModel {
        return CartModel(this.products_name, this.price, this.img, this.weight,this.quant, this.total)
    }
    fun toProductNewModel(): ProductModel {
        return ProductModel(this.products_name, this.price, this.img, this.weight,this.quant, this.total)
    }

}



