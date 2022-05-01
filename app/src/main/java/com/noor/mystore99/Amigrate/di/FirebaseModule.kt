package com.noor.mystore99.Amigrate.di

import com.example.networkmodule.network.Firebase.FirebaseKey
import com.example.networkmodule.network.Firebase.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

    @Provides
    @Singleton
    fun getFirebaseAuth(): FirebaseAuth = FirebaseManager.getFirebaseAuth()


}