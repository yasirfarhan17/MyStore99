package com.noor.mystore99.amigrate.ui.main.fragment.home

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.model.SliderModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.CartRepository
import com.example.networkmodule.repository.ProductRepository
import com.example.networkmodule.usecase.*
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val categoryUseCase: FirebaseGetCategoryUseCase,
    private val insertToCartUseCase: InsertCartItemUseCase,
    private val insertToProductUseCase: InsertProductsUseCase,
    private val cartRepo: CartRepository,
    private val productUseCase: FirebaseGetProductUseCase,
    private val bannerUseCase: FirebaseGetBannerUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val repository: ProductRepository,
    private val productItemsCountUseCase: GetProductcountUseCase

) : BaseViewModel() {

    private var _categoryList = MutableLiveData<ArrayList<CategoryModel>?>()
    val categoryList = _categoryList.toLiveData()

    private var _productItemCount = MutableStateFlow(0)
    val productItemCount: Flow<Int> = _productItemCount

    var _productList = MutableLiveData<ArrayList<ProductEntity>>()
    val productList = _productList.toLiveData()

    private var _bannerList = MutableLiveData<ArrayList<SliderModel>?>()
    val bannerList = _bannerList.toLiveData()


    init {
        launch {
            _viewState.postValue(ViewState.Loading)
            //getAllProducts()
            getProductFromDB()
           // getNewProductFromDB()
           // getProductRepo()
            getBanner()
            getCategory()

        }
    }

    private fun getBanner() {
        launch {
            //_viewState.postValue(ViewState.Loading)
            bannerUseCase.invoke().collectLatest {
                if (it.data.isNullOrEmpty()) {
                    _bannerList.postValue(null)
                    return@collectLatest
                }
                _bannerList.postValue(it.data as ArrayList<SliderModel>)

            }
        }
    }

    fun getProductFromDB(){
        launch {
            _viewState.postValue(ViewState.Loading)
            getAllProductsUseCase.invoke().collectLatest{
               when(it){
                is Resource.Success -> {
                    it.data?.collectLatest {list->
                        _productList.postValue(list as ArrayList<ProductEntity>)
                        //_viewState.postValue(ViewState.Success())
                    }

                        //val list= (it.data as ArrayList<ProductModel>).map {productModel -> productModel.toProductEntity() }
                       // insertProductToDb(list as ArrayList<ProductEntity>)
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


     fun getAllProducts() {
        launch {
            productUseCase.invoke().collect {
                _viewState.postValue(ViewState.Loading)
                when (it) {
                    is Resource.Success -> {
                        val list= it.data?.map {productModel -> productModel.toProductEntity() }
                        _productList.postValue(list as ArrayList<ProductEntity>)
                       // _viewState.postValue(ViewState.Success())

                       // insertProductToDb(list as ArrayList<ProductEntity>)
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


     fun getProductRepo(){
        launch {
            val list=repository.getAllProduct()
            _productList.postValue(list as ArrayList<ProductEntity>)
        }

    }

     fun getProductItemCountUseCase() {
        launch {
            productItemsCountUseCase.invoke().collectLatest {
                when (it) {
                    is Resource.Success -> {
                        if(it.data==null)
                            _productItemCount.value=0
                        else
                            _productItemCount.value=it.data!!
                        Log.d("CartCheck", "" + it)
                    }
                    else -> {}
                }
            }
        }
    }



    private fun getCategory() {
        launch {
            //_viewState.postValue(ViewState.Loading)
            categoryUseCase.invoke().collect {
                if (it.data.isNullOrEmpty()) {
                    _categoryList.postValue(null)
                    return@collect
                }
                _categoryList.postValue(it.data as ArrayList<CategoryModel>)
            }
        }

    }


    fun insertToCartDb(item: CartEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val arr = ArrayList<CartEntity>()
            arr.addAll(cartRepo.getCartItem())
            arr.add(item)
            insertToCartUseCase.invoke(arr)
        }
    }


}