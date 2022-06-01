package com.example.networkmodule.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.networkmodule.database.entity.CartEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartProduct(cart: CartEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCartProduct(cart: List<CartEntity>)

    @Query("delete from cartTable")
    suspend fun clear()
    @Query("delete from cartTable where products_name =:id")
    suspend fun clearIndi(id: String)

    @Query("select * from cartTable")
    suspend fun getAllCartProduct(): List<CartEntity>

    @Query("select * from cartTable where products_name like :id")
    suspend fun searchCartProduct(id :String): CartEntity

    @Query("UPDATE cartTable SET total = :price,quant=:quant WHERE products_name like :id")
    fun update(price: String, id: String,quant:String)

    @Query("SELECT COUNT(*) FROM cartTable")
    fun getCount(): Flow<Int?>
}