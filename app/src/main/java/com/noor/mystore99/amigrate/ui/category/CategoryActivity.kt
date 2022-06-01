package com.noor.mystore99.amigrate.ui.category

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.UserAdapter
import com.noor.mystore99.databinding.ActivityCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


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


            searchView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    viewModel.categoryList.observe(this@CategoryActivity) {
                        val list = it.map { productModel -> productModel.toProductEntity() }
                        val localList=ArrayList<ProductEntity>()
                        var flagNotFound=false
                        if(p0 ==null || p0.isEmpty())
                            localList.addAll(list)
                        else{
                            for(item in list){
                                if(item.products_name.lowercase(Locale.ENGLISH).contains(p0.toString().lowercase(
                                        Locale.ENGLISH))) {
                                    localList.add(item)
                                    flagNotFound=true

                                }
                            }
                        }
                        if(!flagNotFound) {
                            //localList.addAll(list)
                        Toast.makeText(this@CategoryActivity,"No Item Found",Toast.LENGTH_SHORT).show()
                        }

                        (binding.categoryRv.adapter as NewCategoryAdapter).submitList(localList)
                    }
                    //(binding.rvProduct.adapter as UserAdapter).filter.filter(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }
    }





    override fun addObservers() {
        viewModel.categoryList.observe(this@CategoryActivity) {
            val list = it.map { productModel -> productModel.toProductEntity() }
            (binding.categoryRv.adapter as NewCategoryAdapter).submitList(list as ArrayList<ProductEntity>)
        }

    }

    override fun onItemClick(item: CartEntity) {

        viewModel.insertToCartDb(item)
    }
}