package com.example.networkmodule.database.cart

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [CartEntity::class],version = CartDataBase.Version)
abstract class CartDataBase :RoomDatabase() {
    abstract val cart: CartDao

    companion object{
        const val Name="phone_dataBase"
        const val Version=3

        @Volatile
        private var instance : CartDataBase?=null

        fun getInstance(context: Context): CartDataBase {
            val tempInstance= instance
            if(tempInstance!=null)
                return tempInstance

            synchronized(this){
                val instance= Room.databaseBuilder(
                    context,
                    CartDataBase::class.java,
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