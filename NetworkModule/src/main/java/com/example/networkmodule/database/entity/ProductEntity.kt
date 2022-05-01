package com.example.networkmodule.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ProductTable")
data class ProductEntity (
    @PrimaryKey @ColumnInfo var products_name: String,
    @ColumnInfo var price: String,
    @ColumnInfo var img: String,
    @ColumnInfo var quant: String,
    @ColumnInfo var hindiName: String? = null,
    @ColumnInfo var stock: String,
    @ColumnInfo var productType:String

)