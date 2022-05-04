package com.example.networkmodule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "productTable")
data class ProductEntity (
    @PrimaryKey @ColumnInfo val products_name: String,
    @ColumnInfo val price: String,
    @ColumnInfo val img: String,
    @ColumnInfo val quant:String,
    @ColumnInfo val HindiName:String?=null,
    @ColumnInfo val stock:String,
    @ColumnInfo val type:String
        )



