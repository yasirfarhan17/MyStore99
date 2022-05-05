package com.example.networkmodule.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.noor.mystore99.productModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "productTable")
data class ProductEntity (
    @PrimaryKey @ColumnInfo val products_name: String,
    @ColumnInfo val price: String?=null,
    @ColumnInfo val img: String?=null,
    @ColumnInfo val quant:String?=null,
    @ColumnInfo val HindiName:String?=null,
    @ColumnInfo val stock:String?=null,
        ):Parcelable{
            fun toProductModel():productModel{
               return productModel(this.products_name,this.price,this.img,this.quant,this.HindiName,this.stock)


            }

}



