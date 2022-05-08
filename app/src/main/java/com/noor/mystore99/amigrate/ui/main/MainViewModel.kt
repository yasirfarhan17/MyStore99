package com.noor.mystore99.amigrate.ui.main

import android.util.Log
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.GetCartItemsCountUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cartItemCountUseCase: GetCartItemsCountUseCase,
) : BaseViewModel() {

    private var _cartItemCount = MutableStateFlow(0)
    val cartItemCount: Flow<Int> = _cartItemCount

    init {
        getCartItemCountUseCase()
    }


    private fun getCartItemCountUseCase() {
        launch {
            cartItemCountUseCase.invoke().collectLatest {
                when (it) {
                    is Resource.Success -> {
                        it.data?.collectLatest { count ->
                            _cartItemCount.value = count ?: 0
                        }
                        Log.d("CartCheck", "" + it)
                    }
                    else -> {}
                }
            }
        }
    }
}
