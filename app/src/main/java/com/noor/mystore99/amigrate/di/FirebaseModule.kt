package com.noor.mystore99.amigrate.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.networkmodule.database.SbziTazaLocalDatabase
import com.example.networkmodule.database.dao.CartDao
import com.example.networkmodule.database.dao.ProductDao
import com.example.networkmodule.network.FirebaseKey
import com.example.networkmodule.network.FirebaseManager
import com.example.networkmodule.repository.*
import com.example.networkmodule.storage.PrefsStoreImpl
import com.example.networkmodule.storage.PrefsUtil
import com.example.networkmodule.usecase.*
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

    @Named(FirebaseKey.USER_DATABASE_REF)
    @Provides
    @Singleton
    fun getUserRef(
        firebaseDatabase: FirebaseDatabase
    ): DatabaseReference {
        return FirebaseManager.getUserDatabaseRef(firebaseDatabase)
    }

    @Named(FirebaseKey.ORDER_DATABASE_REF)
    @Provides
    @Singleton
    fun getOrderRef(
        firebaseDatabase: FirebaseDatabase
    ): DatabaseReference {
        return FirebaseManager.getOrderDatabaseRef(firebaseDatabase)
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
    fun provideCoilRepository(productDao: ProductDao): ProductRepository {
        return ProductRepositoryImpl(productDao)
    }


    @Provides
    @Singleton
    fun provideUseCase(repository: ProductRepository):
            GetAllProductsUseCase {
        return GetAllProductsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao): CartRepository {
        return CartRepositoryImpl(cartDao)
    }

    @Provides
    @Singleton
    fun provideCheckOutRepository(@Named(FirebaseKey.ORDER_DATABASE_REF) orderRef: DatabaseReference): CheckOutRepository {
        return CheckOutRepositoryImpl(orderRef)
    }
    @Provides
    @Singleton
    fun checkOutUseCase(repository:CheckOutRepository):CheckOutUseCase{
        return CheckOutUseCase(repository)
    }
    @Provides
    @Singleton
    fun productCountUseCase(repository: ProductRepository):GetProductcountUseCase{
        return GetProductcountUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideUserRepository(@Named(FirebaseKey.USER_DATABASE_REF) userRef: DatabaseReference):UserDetail{
        return UserDetailImpl(userRef)
    }

    @Provides
    @Singleton
    fun userUseCase(repository:UserDetail):UserDetailUseCase{
        return UserDetailUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideCartUseCase(repository: CartRepository): GetCartItemsUseCase {
        return GetCartItemsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideInsertCartItemUseCase(repository: CartRepository): InsertCartItemUseCase {
        return InsertCartItemUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideProductDatabase(@ApplicationContext context: Context): SbziTazaLocalDatabase {
        return Room.databaseBuilder(
            context,
            SbziTazaLocalDatabase::class.java,
            SbziTazaLocalDatabase.Name
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideProductDao(productDatabase: SbziTazaLocalDatabase): ProductDao {
        return productDatabase.productDao
    }


    @Provides
    @Singleton
    fun provideCartDao(database: SbziTazaLocalDatabase): CartDao {
        return database.cartDao
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabaseRepository(
        @Named(FirebaseKey.PRODUCT_DATABASE_REF) productDbRef: DatabaseReference,
        @Named(FirebaseKey.CATEGORY_DATABASE_REF) categoryDbRef: DatabaseReference,
        @Named(FirebaseKey.BANNER_DATABASE_REF) bannerDbRef: DatabaseReference,
        @Named(FirebaseKey.CART_DATABASE_REF) cartDbRef: DatabaseReference,
        prefsUtil: PrefsUtil
    ): FirebaseDatabaseRepository {
        return FirebaseDatabaseRepositoryImpl(productDbRef, bannerDbRef, categoryDbRef,cartDbRef,prefsUtil)
    }


    @Provides
    @Singleton
    fun provideFirebaseAllCategoryUseCase(
        repo: FirebaseDatabaseRepository
    ): FireBaseCategoryUseCase {
        return FireBaseCategoryUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideFirebaseGetBannerUseCase(
        repo: FirebaseDatabaseRepository
    ): FirebaseGetBannerUseCase {
        return FirebaseGetBannerUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideFirebaseGetProductUseCase(
        repo: FirebaseDatabaseRepository,
        repository:ProductRepository
    ): FirebaseGetProductUseCase {
        return FirebaseGetProductUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideFirebaseGetCategoryUseCase(
        repo: FirebaseDatabaseRepository
    ): FirebaseGetCategoryUseCase {
        return FirebaseGetCategoryUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideAuthUseCase(
        repo: AuthRepository
    ): GetLoginUseCase {
        return GetLoginUseCase(repo)
    }
    @Provides
    @Singleton
    fun provideCombineUseCase(
        repo: FirebaseDatabaseRepository
    ): CombineUseCase {
        return CombineUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        @Named(FirebaseKey.USER_DATABASE_REF) userDbRef: DatabaseReference,
    ): AuthRepository {
        return AuthRepositoryImpl(userDbRef)
    }

    @Provides
    @Singleton
    fun provideDataStoreImpl(
        @ApplicationContext context: Context
    ): PrefsStoreImpl {
        return PrefsStoreImpl(context)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            PrefsUtil.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun providePrefUtils(sharedPreferences: SharedPreferences): PrefsUtil {
        return PrefsUtil(sharedPreferences)
    }


}