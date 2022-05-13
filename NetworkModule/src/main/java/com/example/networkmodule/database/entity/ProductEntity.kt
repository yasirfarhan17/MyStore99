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
    @ColumnInfo val quant: String? = null,
    @ColumnInfo val HindiName: String? = null,
    @ColumnInfo val stock: String? = null,
    @ColumnInfo var count: Int = 0,
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

    fun toCartEntity(): CartEntity {
        return CartEntity(
            products_name = this.products_name,
            price = this.price,
            img = this.img,
            quant = this.quant,
            count = this.count,
            total = ((this.price?.toInt() ?: 1) * this.count).toString()
        )

    }

}



