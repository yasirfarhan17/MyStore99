package com.example.networkmodule.database.product

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ProductEntity::class],version = ProductDataBase.Version)
abstract class ProductDataBase :RoomDatabase() {
    abstract val product: ProductDao

    companion object{
        const val Name="phone_dataBase"
        const val Version=4

        @Volatile
        private var instance : ProductDataBase?=null

        fun getInstance(context: Context): ProductDataBase {
            val tempInstance= instance
            if(tempInstance!=null)
                return tempInstance

            synchronized(this){
                val instance= Room.databaseBuilder(
                    context,
                    ProductDataBase::class.java,
                    Name
                )
                    .fallbackToDestructiveMigration()
                    .build()
                Companion.instance =instance
                return instance
            }
        }

    }
}