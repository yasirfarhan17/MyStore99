package com.noor.mystore99.amigrate.ui.main.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.model.SliderModel
import com.google.android.material.appbar.AppBarLayout
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseFragment
import com.noor.mystore99.sliderAdapter
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.ui.cart.CartViewModel
import com.noor.mystore99.amigrate.ui.category.CategoryActivity
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.CategoryAdapter
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.CategoryAdapterCallback
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.UserAdapter
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.UserAdapterCallBack
import com.noor.mystore99.amigrate.util.ProgresssDialog
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.databinding.UserFragmentBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class UserFragment : BaseFragment<UserFragmentBinding, UserViewModel>(), UserAdapterCallBack,
    CategoryAdapterCallback  {


    override val viewModel: UserViewModel by viewModels()
    val cartviewModel: CartViewModel by viewModels()
    override fun getViewModelClass(): Class<UserViewModel> = UserViewModel::class.java
    override fun getLayout(): Int = R.layout.user_fragment

    private var onlyViewIsDestroyed = false

    private var sliderModelList = ArrayList<SliderModel>()
    private var currentPage = 2
    private var bannerJob: Job? = null

    private val delayTime: Long = 3000
    private lateinit var progressDialog: ProgresssDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addObservers()
        if (savedInstanceState == null) {
            initUi()
            addListener()
            viewModel.getAllProducts(this.requireContext())
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun addListener() {
        with(binding) {
            txtInputUserId.setEndIconOnClickListener {
                resetSearchState()
            }
            viewMore.setOnClickListener {
                rvCategory.smoothScrollToPosition(rvCategory.adapter?.itemCount?.minus(1) ?: 2)
            }
        }
        setupSearchListener()
    }

    private fun resetSearchState() {
        with(binding) {
            labelPromoOffer.visibility = View.VISIBLE
            rvCategory.visibility = View.VISIBLE
            labelCategory.visibility = View.VISIBLE
            viewPagerBanners.visibility = View.VISIBLE
            searchView.text?.clear()
            labelAllProduct.visibility = View.VISIBLE
        }
    }

    private fun setupSearchListener() {
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                performSearch(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun performSearch(query: String) {
        viewModel.productList.observe(viewLifecycleOwner) { list ->
            if (list == null) return@observe
            
            val filteredList = ArrayList<ProductEntity>()
            if (query.isEmpty()) {
                filteredList.addAll(list)
                setVisibleViewsForSearch(true)
            } else {
                val searchQuery = query.lowercase(Locale.ENGLISH)
                for (item in list) {
                    if (item.products_name.lowercase(Locale.ENGLISH).contains(searchQuery)) {
                        filteredList.add(item)
                    }
                }
                setVisibleViewsForSearch(filteredList.isNotEmpty())
                if (filteredList.isEmpty()) {
                    Toast.makeText(activity, "No Item Found", Toast.LENGTH_SHORT).show()
                }
            }
            (binding.rvProduct.adapter as UserAdapter).submitListNew(filteredList)
        }
    }

    private fun setVisibleViewsForSearch(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        with(binding) {
            labelPromoOffer.visibility = visibility
            rvCategory.visibility = visibility
            labelCategory.visibility = visibility
            viewPagerBanners.visibility = visibility
            labelAllProduct.visibility = visibility
        }
    }


    override fun onStart() {

        binding.rvProduct.layoutManager?.scrollToPosition(0)
        super.onStart()
    }

    private fun initUi() {
        with(binding) {
            rvProduct.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            rvProduct.adapter = UserAdapter(this@UserFragment)

            val gridLayoutManager = GridLayoutManager(context, 2)
            gridLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rvCategory.setLayoutManager(gridLayoutManager)
            binding.rvCategory.adapter = CategoryAdapter(this@UserFragment)
        }
    }


    override fun addObservers() {
        progressDialog = ProgresssDialog(requireContext())
        progressDialog.dismiss()

        viewModel.productList.observe(viewLifecycleOwner) { products ->
            products?.let {
                it.sortBy { productEntity -> productEntity.products_name }
                (binding.rvProduct.adapter as UserAdapter).submitListNew(it)
            }
        }

        viewModel.cartFromDB.observe(viewLifecycleOwner) { cartItems ->
            cartItems?.let {
                Log.d("indideCart", "" + it)
                (binding.rvProduct.adapter as UserAdapter).submitList(it)
            }
        }

        viewModel.bannerList.observe(viewLifecycleOwner) { banners ->
            if (banners.isNullOrEmpty()) {
                binding.viewPagerBanners.setVisible(false)
                binding.labelPromoOffer.setVisible(false)
                return@observe
            }
            binding.viewPagerBanners.setVisible(true)
            binding.labelPromoOffer.setVisible(true)
            sliderModelList = banners
            
            val sliderAdapter = sliderAdapter(sliderModelList)
            binding.viewPagerBanners.adapter = sliderAdapter
            binding.viewPagerBanners.clipToPadding = false
            binding.viewPagerBanners.pageMargin = 20
            binding.viewPagerBanners.currentItem = currentPage
            setBanner()
        }

        viewModel.categoryList.observe(viewLifecycleOwner) { categories ->
            if (categories.isNullOrEmpty()) {
                binding.labelCategory.setVisible(false)
                binding.rvCategory.setVisible(false)
                return@observe
            }
            binding.labelCategory.setVisible(true)
            binding.rvCategory.setVisible(true)
            (binding.rvCategory.adapter as CategoryAdapter).submitList(categories)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setBanner() {
        val onPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) = Unit

            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    pageLooper()
                }
            }
        }
        binding.viewPagerBanners.addOnPageChangeListener(onPageChangeListener)
        startBannerSlideshow()
        binding.viewPagerBanners.setOnTouchListener { _, motionEvent ->
            pageLooper()
            stopBannerSlideShow()
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                startBannerSlideshow()
            }
            false
        }
    }

    private fun pageLooper() {
        if (currentPage == sliderModelList.size - 2) {
            currentPage = 2
            binding.viewPagerBanners.setCurrentItem(currentPage, false)
        }
        if (currentPage == 1) {
            currentPage = sliderModelList.size - 3
            binding.viewPagerBanners.setCurrentItem(currentPage, false)
        }
    }


    private fun startBannerSlideshow() {
        bannerJob?.cancel()
        bannerJob = lifecycleScope.launch {
            while (isActive) {
                delay(delayTime)
                if (currentPage >= sliderModelList.size) {
                    currentPage = 1
                }
                binding.viewPagerBanners.setCurrentItem(currentPage++, true)
            }
        }
    }

    private fun stopBannerSlideShow() {
        bannerJob?.cancel()
    }

    override fun onItemClick(cartEntity: CartEntity) {
        viewModel.insertToCart(cartEntity)
        showToast("Item Added Successfully")
    }


    override fun searchInCartDB(id: String) {
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("VIEW_DESTROYED", true)
    }

    override fun onPause() {
        stopBannerSlideShow()
        super.onPause()
    }
    override fun onDestroyView() {
        stopBannerSlideShow()
        super.onDestroyView()
    }

    override fun onItemClick(productName: String) {
        startActivity(Intent(requireContext(), CategoryActivity::class.java).apply {
            this.putExtra(CategoryActivity.CATEGORY_NAME, productName)
        })
    }

    override fun onClick(price: String, id: String, quant: String) {
        viewModel.updateQuant(price,id,quant)

    }

}
