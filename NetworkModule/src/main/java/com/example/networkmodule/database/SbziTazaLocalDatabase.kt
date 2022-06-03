package com.example.networkmodule.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.networkmodule.database.dao.CartDao
import com.example.networkmodule.database.dao.ProductDao
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity


@Database(
    entities =
    [ProductEntity::class, CartEntity::class], version = SbziTazaLocalDatabase.Version
)
abstract class SbziTazaLocalDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val cartDao: CartDao

    companion object {
        const val Name = "sbzi_taza_database"
        const val Version = 6

        @Volatile
        private var instance: SbziTazaLocalDatabase? = null
        fun getInstance(context: Context): SbziTazaLocalDatabase {
            val tempInstance = instance
            if (tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    SbziTazaLocalDatabase::class.java,
                    Name
                )
                    .fallbackToDestructiveMigration()
                    .build()
                Companion.instance = instance
                return instance
            }
        }

    }
}