package com.noor.mystore99.amigrate.ui.checkout

import android.net.SocketKeepalive
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.model.checkOutModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.CheckOutUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderUseCase: CheckOutUseCase
) : BaseViewModel() {

    private var _checkoutOrder = MutableLiveData<checkOutModel>()
    val checkoutOrder = _checkoutOrder.toLiveData()


    private var _disableCancelButton = MutableLiveData<Boolean>(false )
    val disableCancelButton = _disableCancelButton.toLiveData()

    private var orderTime:String=""



    private fun checkOrderCancellable(){
        launch {
            while (true){
               _disableCancelButton.postValue(checkIfDeliveryCanBeCancelled(dateTime = orderTime))
                delay(1000)
            }
        }
    }

    private fun checkIfDeliveryCanBeCancelled(dateTime: String): Boolean {
        val date = Calendar.getInstance()
        date.time = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).parse(dateTime)!!
        val now = Calendar.getInstance() // Get time now
        val differenceInMillis = now.timeInMillis - date.timeInMillis
        val differenceInHours =
            differenceInMillis / 1000L / 60L / 60L // Divide by millis/sec, secs/min, mins/hr
        if (differenceInHours > 2) {
            return false
        }
        return true
    }

    fun getOrder(key: String, id: String) {
            launch {
                _viewState.postValue(ViewState.Loading)
                orderUseCase.invoke(key, id).collect {
                    when (it) {
                        is Resource.Success -> {
                            if (it.data == null) {
                                _viewState.postValue(ViewState.Error("No order Found"))
                                return@collect
                            }
                            _checkoutOrder.postValue(it.data!!)
                            orderTime=it.data!!.date+" "+it.data!!.time
                            checkOrderCancellable()
                            _viewState.postValue(ViewState.Success())
                        }
                        is Resource.Error -> {
                            _viewState.postValue(ViewState.Error(it.message))
                        }
                        is Resource.Loading -> {
                            _viewState.postValue(ViewState.Loading)
                        }
                    }
                }
            }

        }
    }