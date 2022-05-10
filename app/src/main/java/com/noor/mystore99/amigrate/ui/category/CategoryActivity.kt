package com.noor.mystore99.amigrate.ui.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserViewModel
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.CategoryAdapter
import com.noor.mystore99.databinding.ActivityCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CategoryActivity  : BaseActivity<ActivityCategoryBinding,CategoryViewModel>(),CallBackCategory {

    override val viewModel: CategoryViewModel by viewModels()
     val mainViewModel:UserViewModel by viewModels()
    override fun layoutId(): Int =R.layout.activity_category
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_category)
        initUI()
    }

    private fun initUI(){
        with(binding){
            categoryRv.layoutManager=StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
            categoryRv.adapter = NewCategoryAdapter(this@CategoryActivity)
        }
    }






    override fun addObservers() {
        Log.d("InsideObserve","outside")

        lifecycleScope.launch {

            viewModel.categoryproductList.collect {
                Log.d("InsideObserve","outside1")
                val list=it.map { productModel ->  productModel.toProductEntity() }
                Log.d("InsideObserve",""+it)
                (binding.categoryRv.adapter as NewCategoryAdapter).submitList(list as ArrayList<ProductEntity>)
            }
        }
    }

    override fun onItemClick(cartEntity: CartEntity) {
        mainViewModel.insertToCartDb(cartEntity)
    }
}