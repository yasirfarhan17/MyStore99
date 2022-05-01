package com.example.networkmodule.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.networkmodule.database.dao.ProductDao
import com.example.networkmodule.database.entity.ProductEntity


@Database(entities = [ProductEntity::class],version = ProductDataBase.Version)
abstract class ProductDataBase :RoomDatabase() {
    abstract val phone:ProductDao

    companion object{
        const val Name="sabzi_taza_dataBase"
        const val Version=1

        @Volatile
        private var Instance :ProductDataBase?=null

        fun getInstance(context: Context):ProductDataBase{
            val tempInstance= Instance
            if(tempInstance!=null)
                return tempInstance

            synchronized(this){
                val instance= Room.databaseBuilder(
                    context,
                    ProductDataBase::class.java,
                    Name
                )
                    .build()
                Instance=instance
                return instance
            }
        }

    }
}