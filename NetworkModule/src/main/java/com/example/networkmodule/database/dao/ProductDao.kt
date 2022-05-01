package com.example.networkmodule.database.dao

import androidx.room.*
import com.example.networkmodule.database.entity.ProductEntity


@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProduct(list: List<ProductEntity>)

    @Query("delete from productTable")
    suspend fun clear()

    @Query("select * from productTable")
    suspend fun getAllProduct(): List<ProductEntity>

    @Query("UPDATE productTable Set price = :price  WHERE products_name = :id")
    fun updatePrice(price: String, id: String)

    @Query("SELECT COUNT(*) FROM productTable")
    fun getCount(): Int?

    @Update(entity = ProductEntity::class)
    fun updateProduct(item: ProductEntity)

}