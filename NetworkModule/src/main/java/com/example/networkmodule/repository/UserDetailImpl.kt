package com.example.networkmodule.repository

import com.example.networkmodule.model.UserModel
import com.example.networkmodule.network.FirebaseKey
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

class UserDetailImpl @Inject constructor(
    @Named(FirebaseKey.USER_DATABASE_REF) val getUserRef : DatabaseReference
) : UserDetail {
    override suspend fun getUserDetail(key:String) = callbackFlow<Result<UserModel>> {
        val postValue = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    val key = snapshot.key
//                    tvAddress.text=userModel?.address.toString()
//                    tvPhoneNumber.text=key.toString()
//                    etPincode.setText(userModel?.pincode.toString())
                    this@callbackFlow.trySendBlocking(Result.success(userModel!!))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

        }
        getUserRef.child(key).addValueEventListener(postValue)
        awaitClose {
            getUserRef.removeEventListener(postValue)
        }
    }
}