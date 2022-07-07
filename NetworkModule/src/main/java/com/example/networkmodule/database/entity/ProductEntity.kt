package com.example.networkmodule.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.networkmodule.model.ProductModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "productTable")
data class ProductEntity(
    @PrimaryKey @ColumnInfo val products_name: String,
    @ColumnInfo val price: String? = null,
    @ColumnInfo val img: String? = null,
    @ColumnInfo var quant: String? = null,
    @ColumnInfo val HindiName: String? = null,
    @ColumnInfo val stock: String? = null,
    @ColumnInfo var count: String? = "0",
) : Parcelable {
    fun toProductModel(): ProductModel {
        return ProductModel(
            this.products_name,
            this.price,
            this.img,
            this.quant,
            this.HindiName,
            this.stock,
            this.count
        )


    }

}



