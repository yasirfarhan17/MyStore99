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

    @Query("delete From cartTable WHERE products_name = :id")
    suspend fun deleteItem(id: String)

    @Query("select * from cartTable")
    suspend fun getAllCartProduct(): List<CartEntity>

    @Query("UPDATE cartTable SET price = :price WHERE products_name = :id")
    fun update(price: String, id: String)

    @Query("UPDATE cartTable SET count = :count WHERE products_name = :id")
    fun updateCount(count: Int, id: String)

    @Query("SELECT COUNT(*) FROM cartTable")
    fun getCount(): Flow<Int?>
}