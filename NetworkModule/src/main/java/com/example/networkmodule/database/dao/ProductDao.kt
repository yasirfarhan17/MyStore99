package com.example.networkmodule.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.networkmodule.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(phone: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProduct(phone: List<ProductEntity>)

    @Query("delete from productTable")
    suspend fun clear()

    @Query("select * from productTable")
    fun getProduct(): Flow<List<ProductEntity>>

    @Query("select * from productTable WHERE count>0")
    fun getCartProduct(): Flow<List<ProductEntity>>

    @Query("UPDATE productTable SET price = :price WHERE products_name = :id")
    fun update(price: String, id: String)

    @Query("UPDATE productTable SET count=:count WHERE products_name = :id")
    fun updateCount(count: Int, id: String)

    @Query("SELECT COUNT(*) FROM productTable")
    fun getCount(): Int?

    @Query("SELECT COUNT(*) FROM productTable WHERE count>0")
    fun getCartCount():Flow<Int?>

}