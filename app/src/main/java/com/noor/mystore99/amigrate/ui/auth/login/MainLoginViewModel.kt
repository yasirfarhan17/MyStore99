package com.noor.mystore99.amigrate.ui.auth.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.network.AuthResource
import com.example.networkmodule.network.Resource
import com.example.networkmodule.storage.PrefsStoreImpl
import com.example.networkmodule.usecase.CombineUseCase
import com.example.networkmodule.usecase.GetLoginUseCase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.Model.User
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@HiltViewModel
class MainLoginViewModel @Inject constructor(
    private var loginUseCase: GetLoginUseCase,
    private var prefDataStore: PrefsStoreImpl
) : BaseViewModel() {
    private val _event = MutableLiveData<AuthResource>()
    val event = _event.toLiveData()
    private val _phoneAndPassword = MutableLiveData<Pair<String, String>>()
    val phoneAndPassword = _phoneAndPassword.toLiveData()

    init {
        isLoggedIn()
        //updateUser()
    }

    private fun isLoggedIn() {
        launch {
            prefDataStore.isLoggedIn().collect {
                if (it) {
                    prefDataStore.getPhoneNumber().collect {

                    }
                }
            }
        }
    }




    fun updateUser(){
        val ref=FirebaseDatabase.getInstance().getReference("User")
        val ref1=FirebaseDatabase.getInstance().getReference("UserNew")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val arr=ArrayList<com.example.networkmodule.model.User>()
                snapshot.children.forEach {
                    val  result=it.getValue(User::class.java)
                    val key=it.key

                    val resultNew=com.example.networkmodule.model.User(name = result!!.name,otpVerified = false,uid = key)
                        Log.d("insideUpdate",""+result!!.phone +" "+key)
                       // arr.add(resultNew)
                        ref1.child(result.phone).setValue(resultNew)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun doLogin(phoneNumber: String, password: String) {
        launch {
            _viewState.postValue(ViewState.Loading)
            loginUseCase.invoke(phoneNumber, password).collectLatest {
                Log.d("checkDoLogin", "$phoneNumber $password")
                when (it) {
                    is AuthResource.Error -> _viewState.postValue(ViewState.Error(it.error))
                    AuthResource.InvalidPhoneNumber -> _viewState.postValue(ViewState.Error("Please Enter Correct Phone number"))
                    AuthResource.Loading -> _viewState.postValue(ViewState.Loading)
                    AuthResource.NoUserFound -> _viewState.postValue(ViewState.Error("No  User Found"))
                    AuthResource.OtpRequired -> {}
                    AuthResource.OtpSend -> {}
                    AuthResource.Success -> {
                        _event.postValue(AuthResource.Success)
                        _viewState.postValue(ViewState.Success())
                    }
                    is AuthResource.VerificationFailed -> _viewState.postValue(ViewState.Error("Verification Failed"))
                    AuthResource.WrongPassword -> _viewState.postValue(ViewState.Error("Please enter correct password"))
                }
            }
        }
    }
}