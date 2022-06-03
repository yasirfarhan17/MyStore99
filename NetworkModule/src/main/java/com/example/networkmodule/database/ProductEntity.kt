package com.example.networkmodule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "productTable")
data class ProductEntity (
    @PrimaryKey @ColumnInfo val products_name: String,
    @ColumnInfo val price: String?=null,
    @ColumnInfo val img: String?=null,
    @ColumnInfo val quant:String?=null,
    @ColumnInfo val HindiName:String?=null,
    @ColumnInfo val stock:String?=null,
    @ColumnInfo val type:String?=null
)



