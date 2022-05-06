package com.noor.mystore99.amigrate.ui.main.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.networkmodule.database.ProductEntity
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseFragment
import com.noor.mystore99.amigrate.ui.main.MainViewModel
import com.noor.mystore99.databinding.UserFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : BaseFragment<UserFragmentBinding, UserViewModel>() {
    private val mainViewModel: MainViewModel by activityViewModels()


    override val viewModel: UserViewModel by viewModels()
    override fun getViewModelClass(): Class<UserViewModel> = UserViewModel::class.java
    override fun getLayout(): Int = R.layout.user_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root
        init()
    }

    private fun init() {
        with(binding) {
            rvProduct.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            mainViewModel.getProductFromDB()
            rvProduct.adapter = UserAdapter(this@UserFragment)
        }
    }

    override fun addObservers() {
        mainViewModel.productList.observe(viewLifecycleOwner) {
           val list= it.map { it.toProductEntity() }
            (binding.rvProduct.adapter as UserAdapter).submitList(list as ArrayList<ProductEntity>)
        }
    }

}