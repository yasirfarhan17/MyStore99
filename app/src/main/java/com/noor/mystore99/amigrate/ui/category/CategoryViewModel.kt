package com.noor.mystore99.amigrate.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.CartRepository
import com.example.networkmodule.usecase.FireBaseCategoryUseCase
import com.example.networkmodule.usecase.InsertCartItemUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryUseCase: FireBaseCategoryUseCase,
    private val insertToCartUseCase: InsertCartItemUseCase,
    private val cartRepo: CartRepository
) : BaseViewModel() {

    private val _categoryList = MutableLiveData<List<ProductEntity>>()
    val categoryList: LiveData<List<ProductEntity>> = _categoryList

    private var originalList: List<ProductEntity> = emptyList()

    fun getAllCategory(productName: String) {
        launch {
            _viewState.postValue(ViewState.Loading)
            categoryUseCase.invoke(productName).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val data = resource.data
                        if (data.isNullOrEmpty()) {
                            _viewState.postValue(ViewState.Error("No Product Found"))
                        } else {
                            val mappedList = data.map { it.toProductEntity() }
                            originalList = mappedList
                            _categoryList.postValue(mappedList)
                            _viewState.postValue(ViewState.Success())
                        }
                    }
                    is Resource.Error -> {
                        _viewState.postValue(ViewState.Error(resource.message))
                    }
                    is Resource.Loading -> {
                        _viewState.postValue(ViewState.Loading)
                    }
                }
            }
        }
    }

    fun filterProducts(query: String?) {
        if (query.isNullOrEmpty()) {
            _categoryList.value = originalList
            return
        }
        val lowerCaseQuery = query.lowercase(Locale.ENGLISH)
        val filtered = originalList.filter { item ->
            item.products_name.lowercase(Locale.ENGLISH).contains(lowerCaseQuery)
        }
        _categoryList.value = filtered
    }

    fun insertToCartDb(item: CartEntity) {
        launch {
            withContext(Dispatchers.IO) {
                val currentCart = ArrayList(cartRepo.getCartItem())
                currentCart.add(item)
                insertToCartUseCase.invoke(currentCart)
            }
        }
    }
}