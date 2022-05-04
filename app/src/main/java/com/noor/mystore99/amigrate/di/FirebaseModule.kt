package com.noor.mystore99.amigrate.di

import android.content.Context
import androidx.room.Room
import com.example.networkmodule.database.ProductDao
import com.example.networkmodule.database.ProductDataBase
import com.example.networkmodule.network.FirebaseKey
import com.example.networkmodule.network.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {


    @Provides
    @Singleton
    fun getFirebaseDataBase(): FirebaseDatabase {
        return FirebaseManager.getFirebaseInstance()
    }

    @Named(FirebaseKey.PRODUCT_DATABASE_REF)
    @Provides
    @Singleton
    fun getAllProductDataBase(
        firebaseDatabase: FirebaseDatabase
    ): DatabaseReference {
        return FirebaseManager.getProductDatabaseRef(firebaseDatabase)
    }

    @Provides
    @Singleton
    fun getMainDatabase(
        firebaseDatabase: FirebaseDatabase
    ): DatabaseReference {
        return FirebaseManager.getMainDatabaseRef(firebaseDatabase)
    }

    @Named(FirebaseKey.BANNER_DATABASE_REF)
    @Provides
    @Singleton
    fun getBannerRef(
        firebaseDatabase: FirebaseDatabase
    ): DatabaseReference {
        return FirebaseManager.getBannerDatabaseRef(firebaseDatabase)
    }

    @Named(FirebaseKey.CART_DATABASE_REF)
    @Provides
    @Singleton
    fun getCartRef(
        firebaseDatabase: FirebaseDatabase
    ): DatabaseReference {
        return FirebaseManager.getCartDatabaseRef(firebaseDatabase)
    }

    @Named(FirebaseKey.CATEGORY_DATABASE_REF)
    @Provides
    @Singleton
    fun getCategoryRef(
        firebaseDatabase: FirebaseDatabase
    ): DatabaseReference {
        return FirebaseManager.getCategoryRef(firebaseDatabase)
    }

    @Provides
    @Singleton
    fun getFirebaseAuth(): FirebaseAuth = FirebaseManager.getFirebaseAuth()


    @Provides
    @Singleton
    fun provideProductDatabase(@ApplicationContext context:Context):ProductDataBase{
        return Room.databaseBuilder(
            context,
            ProductDataBase::class.java,
            ProductDataBase.Name
        )
            .build()

    }

    @Provides
    @Singleton
    fun provideProductDao(productDatabase:ProductDataBase):ProductDao{
        return productDatabase.product
    }

}