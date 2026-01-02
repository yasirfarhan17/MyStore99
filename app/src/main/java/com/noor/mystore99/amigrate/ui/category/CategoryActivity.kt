package com.noor.mystore99.amigrate.ui.category

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.networkmodule.database.entity.CartEntity
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserViewModel
import com.noor.mystore99.databinding.ActivityCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryActivity : BaseActivity<ActivityCategoryBinding, CategoryViewModel>(), CategoryCallBack {

    companion object {
        const val CATEGORY_NAME = "CATEGORY_NAME"
    }

    override val viewModel: CategoryViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun layoutId(): Int = R.layout.activity_category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        processIntent()
        initUI()
    }

    private fun processIntent() {
        intent.getStringExtra(CATEGORY_NAME)?.let { name ->
            viewModel.getAllCategory(name)
        }
    }

    private fun initUI() {
        with(binding) {
            categoryRv.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            categoryRv.adapter = NewCategoryAdapter(this@CategoryActivity)

            searchView.addTextChangedListener { text ->
                viewModel.filterProducts(text.toString())
            }

            back.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun addObservers() {
        viewModel.categoryList.observe(this) { list ->
            (binding.categoryRv.adapter as NewCategoryAdapter).submitListNew(ArrayList(list))
        }
    }

    override fun onItemClick(cartEntity: CartEntity) {
        userViewModel.insertToCart(cartEntity)
        showToast("Item Added Successfully")
    }

    override fun onClick(price: String, id: String, quant: String) {
        userViewModel.updateQuant(price, id, quant)
    }
}