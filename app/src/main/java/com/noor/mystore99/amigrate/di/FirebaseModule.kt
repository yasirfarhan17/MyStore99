package com.noor.mystore99.amigrate.di

import android.content.Context
import androidx.room.Room
import com.example.networkmodule.database.cart.CartDao
import com.example.networkmodule.database.cart.CartDataBase
import com.example.networkmodule.database.product.ProductDao
import com.example.networkmodule.database.product.ProductDataBase
import com.example.networkmodule.network.FirebaseKey
import com.example.networkmodule.network.FirebaseManager
import com.example.networkmodule.repository.CartRepository
import com.example.networkmodule.repository.CartRepositoryImpl
import com.example.networkmodule.repository.ProductRepository
import com.example.networkmodule.repository.ProductRepositoryImpl
import com.example.networkmodule.usecase.CartUseCase
import com.example.networkmodule.usecase.ProductUseCase
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
    fun provideCoilRepository(productDao: ProductDao):ProductRepository{
        return ProductRepositoryImpl(productDao)
    }



    @Provides
    @Singleton
    fun provideUseCase(repository: ProductRepository):
            ProductUseCase{
        return ProductUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao):CartRepository
    {
        return CartRepositoryImpl(cartDao)
    }
    @Singleton
    @Provides
    fun provideCartUseCase(repository: CartRepository):CartUseCase{
        return CartUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideProductDatabase(@ApplicationContext context: Context): ProductDataBase {
        return Room.databaseBuilder(
            context,
            ProductDataBase::class.java,
            ProductDataBase.Name
        )
            .fallbackToDestructiveMigration()
            .build()

    }

    @Provides
    @Singleton
    fun provideProductDao(productDatabase: ProductDataBase): ProductDao {
        return productDatabase.product
    }

    @Provides
    @Singleton
    fun provideCartDatabase(@ApplicationContext context: Context):CartDataBase{
        return Room.databaseBuilder(
            context,
            CartDataBase::class.java,
            CartDataBase.Name
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCartDao(cartDataBase: CartDataBase):CartDao{
        return cartDataBase.cart
    }

}