package com.example.networkmodule.network


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseManager {

    fun getFirebaseInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    fun getMainDatabaseRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }

    fun getProductDatabaseRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.reference.child(FirebaseKey.GET_ALL_PRODUCT_KEY)
    }

    fun getBannerDatabaseRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.reference.child(FirebaseKey.GET_BANNER_KEY)
    }

    fun getCartDatabaseRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.reference.child(FirebaseKey.GET_CART_KEY)
    }

    fun getFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    fun getCategoryRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {

        return firebaseDatabase.reference.child(FirebaseKey.GET_CATEGORY_KEY)
    }

}