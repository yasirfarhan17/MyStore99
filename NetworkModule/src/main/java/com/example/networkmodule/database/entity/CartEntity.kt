package com.example.networkmodule.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.networkmodule.model.CartModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cartTable")
data class CartEntity(
    @PrimaryKey @ColumnInfo val products_name: String,
    @ColumnInfo val price: String? = null,
    @ColumnInfo val img: String? = null,
    @ColumnInfo val quant: String? = null,
    @ColumnInfo val total: String? = null,
) : Parcelable {
    fun toProductModel(): CartModel {
        return CartModel(this.products_name, this.price, this.img, this.quant, this.total)
    }

}



