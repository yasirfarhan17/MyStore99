package com.example.networkmodule.repository

import com.example.networkmodule.model.User
import com.example.networkmodule.network.AuthResource
import com.example.networkmodule.network.FirebaseKey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(
    @Named(FirebaseKey.GET_USER_KEY) private val userDbRef: DatabaseReference,
) : AuthRepository {


    override suspend fun login(phoneNumber: String, password: String) = callbackFlow {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(AuthResource.Error(error.message))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()
                        .not() || dataSnapshot.value == null || dataSnapshot.key == null
                ) {
                    val obj = User("SAHIL PARWEZ", "123", "wcewvhvhechvc", true)
                    userDbRef.child(phoneNumber).setValue(obj)
                    this@callbackFlow.trySendBlocking(AuthResource.NoUserFound)
                    return
                }
                val obj = dataSnapshot.getValue(User::class.java)
                if (obj?.password == password) {
                    if (obj.otpVerified) {
                        //TODO save data to share pref or datastore
                        this@callbackFlow.trySendBlocking(AuthResource.Success)
                    } else {
                        this@callbackFlow.trySendBlocking(AuthResource.OtpRequired)
                    }
                } else this@callbackFlow.trySendBlocking(AuthResource.WrongPassword)
            }
        }

        userDbRef.child(phoneNumber).addValueEventListener(postListener)
        awaitClose {
            userDbRef.removeEventListener(postListener)
        }
    }

}