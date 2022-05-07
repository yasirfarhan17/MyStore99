package com.example.networkmodule.database.cart

import androidx.room.*


@Dao
interface CartDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(cart:CartEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProduct(cart: List<CartEntity>)

    @Query("delete from cartTable")
    suspend fun clear()

    @Query("select * from cartTable")
    suspend fun getProduct():List<CartEntity>

    @Query("UPDATE cartTable SET price = :price WHERE products_name = :id")
    fun update(price:String, id: String)

    @Query("SELECT COUNT(*) FROM cartTable")
    fun getCount(): Int?




}