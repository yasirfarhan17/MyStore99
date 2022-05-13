package com.noor.mystore99.amigrate.ui.main.fragment.home

import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.model.SliderModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.CartRepository
import com.example.networkmodule.repository.FirebaseDatabaseRepository
import com.example.networkmodule.repository.ProductRepository
import com.example.networkmodule.usecase.*
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val categoryUseCase: FirebaseGetCategoryUseCase,
    private val insertToCartUseCase: InsertCartItemUseCase,
    private val cartRepo: CartRepository,
    private val productUseCase: FirebaseGetProductUseCase,
    private val bannerUseCase: FirebaseGetBannerUseCase,
    private val productRepo: ProductRepository,
    private val firebaseRepo: FirebaseDatabaseRepository,
) : BaseViewModel() {

    private var _categoryList = MutableLiveData<ArrayList<CategoryModel>?>()
    val categoryList = _categoryList.toLiveData()


    private var _productList = MutableLiveData<ArrayList<ProductModel>>()
    val productList = _productList.toLiveData()

    private var _bannerList = MutableLiveData<ArrayList<SliderModel>?>()
    val bannerList = _bannerList.toLiveData()


    init {
        launch {
            _viewState.postValue(ViewState.Loading)
            getAllProducts()
            getBanner()
            getCategory()
        }
    }

    private fun getBanner() {
        launch {
            _viewState.postValue(ViewState.Loading)
            bannerUseCase.invoke().collectLatest {
                if (it.data.isNullOrEmpty()) {
                    _bannerList.postValue(null)
                    return@collectLatest
                }
                _bannerList.postValue(it.data as ArrayList<SliderModel>)

            }
        }
    }

    private fun getAllProducts() {
        launch {
            _viewState.postValue(ViewState.Loading)
            firebaseRepo.getAllProductCount().collect {
                if (it.isSuccess) {
                    if ((it.getOrNull() ?: 0).toInt() == productRepo.getNoOfProducts()) {
                        productRepo.getAllProduct().collect { productEntityList ->
                            val localList =
                                productEntityList.map { productEntity -> productEntity.toProductModel() } as ArrayList
                            _productList.postValue(localList)
                            _viewState.postValue(ViewState.Success())
                        }
                    } else {
                        getProductFromNetwork()
                    }
                } else {
                    getProductFromNetwork()
                }
            }

        }
    }

    private suspend fun getProductFromNetwork() {
        productUseCase.invoke().collect { dbList ->
            when (dbList) {
                is Resource.Success -> {
                    _productList.postValue(dbList.data as ArrayList<ProductModel>)
                    val listToInDb = dbList.data?.map { it.toProductEntity() }
                    listToInDb?.let { productRepo.insertItems(it) }
                    _viewState.postValue(ViewState.Success())
                }
                is Resource.Error -> {
                    _viewState.postValue(ViewState.Error(dbList.message))
                }
                is Resource.Loading -> {
                    _viewState.postValue(ViewState.Loading)
                }
            }
        }

    }

    private fun getCategory() {
        launch {
            categoryUseCase.invoke().collect {
                if (it.data.isNullOrEmpty()) {
                    _categoryList.postValue(null)
                    return@collect
                }
                _categoryList.postValue(it.data as ArrayList<CategoryModel>)
            }
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
            insertToCartDb(item.toCartEntity())
        }
    }

    /**
     * When someone update increase or decrease item quantity ->
     * 1-> Product Db -> update that particular item count
     * 2-> Cart Db -> update that particular item count
     * */
    fun updateItemToCart(item: ProductEntity) {
        launch {
            productRepo.updateCount(item.count, item.products_name)
            if(item.count==0){
                cartRepo.deleteItem(item.products_name)
            }else cartRepo.updateCount(item.count, item.products_name)
        }
    }


    private fun insertToCartDb(item: CartEntity) {
        launch {
            val arr = ArrayList<CartEntity>()
            arr.addAll(cartRepo.getCartItem())
            arr.add(item)
            insertToCartUseCase.invoke(arr)
        }
    }

    fun deleteItemFormCartDb(item: ProductEntity) {
        launch {
            cartRepo.deleteItem(item.products_name)
        }
    }

}