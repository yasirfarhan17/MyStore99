package com.example.networkmodule.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.noor.mystore99.productModel


@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(phone: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProduct(phone: List<ProductEntity>)

    @Query("delete from productTable")
    suspend fun clear()

    @Query("select * from productTable")
    suspend fun getProduct():List<ProductEntity>

    @Query("UPDATE productTable SET price = :price WHERE products_name = :id")
    fun update(price:String, id: String)

    @Query("SELECT COUNT(*) FROM productTable")
    fun getCount(): Int?

}