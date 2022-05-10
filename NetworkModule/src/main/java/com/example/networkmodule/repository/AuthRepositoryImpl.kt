package com.example.networkmodule.repository

import com.example.networkmodule.network.FirebaseKey
import com.example.networkmodule.network.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(
    @Named(FirebaseKey.GET_USER_KEY) private val userDbRef: DatabaseReference,
) : AuthRepository {


    override suspend fun login(
        phoneNumber: String,
        password: String
    ): Flow<Resource<DataSnapshot>> =
        callbackFlow {
            val postListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(Resource.Error(error.message.toString()))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    this@callbackFlow.trySendBlocking(Resource.Success(dataSnapshot))
                }
            }
            userDbRef.child(phoneNumber).addValueEventListener(postListener)
            awaitClose {
                userDbRef.removeEventListener(postListener)
            }
        }

}