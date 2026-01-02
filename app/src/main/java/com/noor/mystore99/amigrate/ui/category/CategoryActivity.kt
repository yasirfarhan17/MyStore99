package com.noor.mystore99.amigrate.ui.category

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
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
    
    private lateinit var categoryAdapter: NewCategoryAdapter
    private var categoryName: String = ""

    override fun layoutId(): Int = R.layout.activity_category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        processIntent()
        setupUI()
    }

    private fun processIntent() {
        categoryName = intent.getStringExtra(CATEGORY_NAME) ?: "Category"
        binding.tvCategoryName.text = categoryName
        viewModel.getAllCategory(categoryName)
    }

    private fun setupUI() {
        // Back button
        binding.imgBack.setOnClickListener { finish() }
        
        // RecyclerView setup with Grid
        categoryAdapter = NewCategoryAdapter(this)
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(this@CategoryActivity, 2)
            adapter = categoryAdapter
        }
        
        // Search functionality
        binding.searchView.addTextChangedListener { text ->
            val query = text.toString()
            viewModel.filterProducts(query)
        }
    }

    override fun addObservers() {
        viewModel.categoryList.observe(this) { products ->
            if (products.isNullOrEmpty()) {
                binding.rvProducts.visibility = View.GONE
                binding.emptyStateLayout.visibility = View.VISIBLE
            } else {
                binding.rvProducts.visibility = View.VISIBLE
                binding.emptyStateLayout.visibility = View.GONE
                categoryAdapter.submitListNew(ArrayList(products))
            }
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