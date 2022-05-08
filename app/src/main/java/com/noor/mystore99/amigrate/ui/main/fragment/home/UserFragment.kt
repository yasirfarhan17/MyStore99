package com.noor.mystore99.amigrate.ui.main.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.model.CategoryModel
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseFragment
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.CategoryAdapter
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.UserAdapter
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.databinding.UserFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserFragment : BaseFragment<UserFragmentBinding, UserViewModel>() {
    //private val mainViewModel: MainViewModel by activityViewModels()
    override val viewModel: UserViewModel by viewModels()
    override fun getViewModelClass(): Class<UserViewModel> = UserViewModel::class.java
    override fun getLayout(): Int = R.layout.user_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root
        init()
    }

    override fun onStart() {
        Log.d("USER_FRAGMENT", "onStart")
        binding.rvProduct.layoutManager?.scrollToPosition(0)
        super.onStart()
    }

    private fun init() {
        with(binding) {
            rvProduct.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            rvProduct.adapter = UserAdapter()
            rvCategory.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvCategory.adapter = CategoryAdapter()
        }
    }

    override fun addObservers() {
        viewModel.productList.observe(viewLifecycleOwner) {
            val list = it.map { productModel -> productModel.toProductEntity() }
            (binding.rvProduct.adapter as UserAdapter).submitList(
                list as ArrayList<ProductEntity>,
                viewModel
            )
        }
        viewModel.categoryList.observe(viewLifecycleOwner) {
            if(it.isNullOrEmpty()){
                binding.labelCategory.setVisible(false)
                binding.rvCategory.setVisible(false)
                return@observe
            }
            binding.labelCategory.setVisible(true)
            binding.rvCategory.setVisible(true)
            (binding.rvCategory.adapter as CategoryAdapter).submitList(it as ArrayList<CategoryModel>)
        }

    }

}
