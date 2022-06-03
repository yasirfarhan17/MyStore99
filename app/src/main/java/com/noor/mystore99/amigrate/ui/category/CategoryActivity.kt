package com.noor.mystore99.amigrate.ui.category

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.databinding.ActivityCategoryBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryActivity : BaseActivity<ActivityCategoryBinding, CategoryViewModel>(),
    CategoryCallBack {

    companion object {
        const val CATEGORY_NAME = "CATEGORY_NAME"
    }

    override val viewModel: CategoryViewModel by viewModels()

    override fun layoutId(): Int = R.layout.activity_category
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        getData()
        initUI()

    }

    private fun getData() {
        if (intent.getStringExtra(CATEGORY_NAME) != null) {
            viewModel.getAllCategory(intent.getStringExtra(CATEGORY_NAME)!!)
        }
    }

    private fun initUI() {
        with(binding) {
            categoryRv.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            categoryRv.adapter = NewCategoryAdapter(this@CategoryActivity)
        }
    }


    override fun addObservers() {
        viewModel.categoryList.observe(this@CategoryActivity) {
            val list = it.map { productModel -> productModel.toProductEntity() }
            (binding.categoryRv.adapter as NewCategoryAdapter).submitList(list as ArrayList<ProductEntity>)
        }

    }

    override fun onItemClick(item: ProductEntity) {
        val cartEntity =
            CartEntity(item.products_name, item.price, item.img, item.quant, item.price)
        viewModel.insertToCartDb(cartEntity)
    }
}