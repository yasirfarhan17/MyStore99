package com.noor.mystore99.amigrate.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.repository.ProductRepository
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    // private val getCartItemsUseCase: GetCartItemsUseCase,
    // private val insertToCartUseCase: InsertCartItemUseCase,
    private val productRepo: ProductRepository,
    //private val cartRepo: CartRepository,
) : BaseViewModel() {

    private var _cartFromDB = MutableLiveData<ArrayList<CartEntity>>()
    val cartFromDB = _cartFromDB.toLiveData()

    private var clearCartFlag: Boolean = false

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCartFromProductDb()
        }
    }

    private suspend fun getCartFromProductDb() {
        launch {
            productRepo.getAllCartProduct().collectLatest {
                if (clearCartFlag) return@collectLatest
                _cartFromDB.postValue(it.map { productEntity -> productEntity.toCartEntity() } as ArrayList<CartEntity>)
            }
        }
    }

/*
    private suspend fun getCartFromDB() {
        launch {
            _viewState.postValue(ViewState.Loading)
            getCartItemsUseCase().collectLatest {
                when (it) {
                    is Resource.Success -> {
                        _cartFromDB.postValue(it.data as ArrayList<CartEntity>)
                        _viewState.postValue(ViewState.Success())
                    }
                    is Resource.Error -> {
                        _viewState.postValue(ViewState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        _viewState.postValue(ViewState.Loading)
                    }
                    else -> {
                        Log.d("yas", "g")
                    }
                }
            }
        }
    }
*/


    /**
     * When someone clears the cart->
     * 1-> Product Db -> update that particular item count to 0 by matching it with catItem list ids
     * 2-> Cart Db -> delete all data from db
     * */
/*
    fun clearCart() {
        launch {
            _viewState.postValue(ViewState.Loading)
            cartRepo.getCartItem().forEach {
                productRepo.updateCount(0, it.products_name)
            }
            cartRepo.clearCart()
            _viewState.postValue(ViewState.Success())
        }
    }
*/

    fun clearCart() {
        launch {
            _viewState.postValue(ViewState.Loading)
            /* clearCartFlag = true
             productRepo.getAllCartProduct().collectLatest { cartItems ->
                 if (clearCartFlag) return@collectLatest
                 cartItems.forEach {
                     productRepo.updateCount(0, it.products_name)
                 }
                 _viewState.postValue(ViewState.Success())
                 clearCartFlag = false
             }*/
            clearCartFlag = true
            cartFromDB.value?.forEach {
                productRepo.updateCount(0, it.products_name)
            }
            _viewState.postValue(ViewState.Success())
            clearCartFlag = false
        }
    }

    /**
     * When someone add item to cart->
     * 1-> Product Db -> update that particular item count
     * 2-> Cart Db -> Add that item to cart
     * */
    fun inertItemToCart(item: ProductEntity) {
        launch {
            productRepo.updateCount(item.count, item.products_name)
            //  insertToCartDb(item.toCartEntity())
        }
    }


    /**
     * When someone update increase or decrease item quantity ->
     * 1-> Product Db -> update that particular item count
     * 2-> Cart Db -> update that particular item count
     * */
    fun updateItemToCart(item: ProductEntity) {
        launch {
            _viewState.postValue(ViewState.Loading)
            productRepo.updateCount(item.count, item.products_name)
            //cartRepo.updateCount(item.count, item.products_name)
            delay(200)
            _viewState.postValue(ViewState.Success())

        }
    }

/*
    private fun insertToCartDb(item: CartEntity) {
        launch {
            val arr = ArrayList<CartEntity>()
            arr.addAll(cartRepo.getCartItem())
            arr.add(item)
            insertToCartUseCase.invoke(arr)
        }
    }
*/


}