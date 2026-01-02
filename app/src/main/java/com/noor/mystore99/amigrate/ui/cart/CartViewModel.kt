package com.noor.mystore99.amigrate.ui.cart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.dao.CartDao
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.CartModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.ClearCartItemsUseCase
import com.example.networkmodule.usecase.DeleteCartItemUseCase
import com.example.networkmodule.usecase.FireBaseCartUseCase
import com.example.networkmodule.usecase.GetCartItemsUseCase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val clearCartItemsUseCase: ClearCartItemsUseCase,
    private val getCart: FireBaseCartUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,


    private val dao: CartDao
) : BaseViewModel() {

    private var _cartFromDB = MutableLiveData<ArrayList<CartEntity>>()
    val cartFromDB = _cartFromDB.toLiveData()

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice = _totalPrice.toLiveData()

    private val _deliveryCharge = MutableLiveData<Int>()
    val deliveryCharge = _deliveryCharge.toLiveData()

    private val _subTotal = MutableLiveData<Int>()
    val subTotal = _subTotal.toLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCartFromDB()
        }
    }


    suspend fun getCartFromDB() {
        launch {
            _viewState.postValue(ViewState.Loading)
            getCart().collectLatest {
                when (it) {
                    is Resource.Success -> {
                        Log.d("indideCartViewModel",it.data.toString())
                        val cartList = it.data as ArrayList<CartEntity>
                        _cartFromDB.postValue(cartList)
                        calculateTotals(cartList)
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

    private fun calculateTotals(cartList: List<CartEntity>) {
        var subValue = 0
        for (item in cartList) {
            subValue += item.total?.toIntOrNull() ?: 0
        }
        _subTotal.postValue(subValue)

        val charge = when (subValue) {
            in 401..1000 -> 10
            in 100..400 -> 20
            else -> 0
        }
        _deliveryCharge.postValue(charge)
        _totalPrice.postValue(subValue + charge)
    }

    fun updateQuant(price: String, id: String, quant: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(price, id, quant)
            // Ideally we re-fetch or logic updates automatically if Flow used. 
            // For now assuming cartDataCall listener handles updates or we rely on Firebase listener.
        }
    }

    fun clearCart() {
        launch {
            _viewState.postValue(ViewState.Loading)
            clearCartItemsUseCase.invoke()
            delay(300)
            _cartFromDB.postValue(ArrayList())
            calculateTotals(emptyList())
            _viewState.postValue(ViewState.Success())

        }
    }

    fun deleteItemFromCart(cartItem:CartEntity){
        launch {
            _viewState.postValue(ViewState.Loading)
            deleteCartItemUseCase.invoke(cartItem).collectLatest {
                _viewState.postValue(ViewState.Success())
                // Optimistic update or wait for firestore callback logic to update list
            }
        }
    }
    
    fun clearCartForUser(key: String) {
        FirebaseDatabase.getInstance().getReference("CartNew").child(key).removeValue()
        _cartFromDB.postValue(ArrayList())
        calculateTotals(emptyList())
    }

    fun clear(id: String) {
        launch {
            dao.clearIndi(id)
        }
    }
    
    fun cartDataCall(key:String){
        val ref= FirebaseDatabase.getInstance().getReference("CartNew").child(key)
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartList = ArrayList<CartEntity>()
                snapshot.children.forEach {
                    Log.d("SAHIL_CART", "cart $it")
                    val cartItem = it.getValue(CartModel::class.java)?.toCartEntity()
                    cartItem?.let { item ->
                        cartList.add(item)
                    }
                }
                _cartFromDB.postValue(cartList)
                calculateTotals(cartList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CartViewModel", "Database error: ${error.message}")
            }
        })
    }
}