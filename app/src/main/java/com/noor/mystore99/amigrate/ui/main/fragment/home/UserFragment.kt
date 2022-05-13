package com.noor.mystore99.amigrate.ui.main.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.model.SliderModel
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseFragment
import com.noor.mystore99.amigrate.ui.category.CategoryActivity
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.CategoryAdapter
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.CategoryAdapterCallback
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.UserAdapter
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.UserAdapterCallBack
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.databinding.UserFragmentBinding
import com.noor.mystore99.sliderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class UserFragment : BaseFragment<UserFragmentBinding, UserViewModel>(), UserAdapterCallBack,
    CategoryAdapterCallback {


    override val viewModel: UserViewModel by viewModels()
    override fun getViewModelClass(): Class<UserViewModel> = UserViewModel::class.java
    override fun getLayout(): Int = R.layout.user_fragment

    var sliderModelList = ArrayList<SliderModel>()
    private var currentPage = 2
    private var timer: Timer? = null
    private val delayTime: Long = 3000
    private val periodTime: Long = 3000

    private var firstTime=true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root
        init()
        addListener()
    }

    private fun addListener() {
        binding.searchView.setOnQueryTextFocusChangeListener { view, b ->
            binding.labelPromoOffer.setVisible(b)
            binding.rvCategory.setVisible(b)
            binding.labelCategory.setVisible(b)
            binding.viewPagerBanners.setVisible(b)
        }
    }

    override fun onStart() {
        binding.rvProduct.layoutManager?.scrollToPosition(0)
        super.onStart()
    }

    private fun init() {
        with(binding) {
            rvProduct.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            rvProduct.adapter = UserAdapter(this@UserFragment)
            rvCategory.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvCategory.adapter = CategoryAdapter(this@UserFragment)
        }
    }

    override fun addObservers() {
        viewModel.productList.observe(viewLifecycleOwner) {
            if (firstTime.not()) return@observe
            showProgress()
            val list = it.map { productModel -> productModel.toProductEntity() }
            (binding.rvProduct.adapter as UserAdapter).submitList(list as ArrayList<ProductEntity>)
            firstTime=false
            hideProgress()
        }
        viewModel.bannerList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.viewPagerBanners.setVisible(false)
                binding.labelPromoOffer.setVisible(false)
                return@observe
            }
            binding.viewPagerBanners.setVisible(true)
            binding.labelPromoOffer.setVisible(true)
            sliderModelList = it
            val sliderAdapter = sliderAdapter(sliderModelList)
            binding.viewPagerBanners.adapter = sliderAdapter
            binding.viewPagerBanners.clipToPadding = false
            binding.viewPagerBanners.pageMargin = 20
            sliderAdapter.notifyDataSetChanged()
            binding.viewPagerBanners.currentItem = currentPage
            setBanner()
        }
        viewModel.categoryList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.labelCategory.setVisible(false)
                binding.rvCategory.setVisible(false)
                return@observe
            }
            binding.labelCategory.setVisible(true)
            binding.rvCategory.setVisible(true)
            (binding.rvCategory.adapter as CategoryAdapter).submitList(it)
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
        val handler = Handler()
        val update = Runnable {
            if (currentPage >= sliderModelList.size) {
                currentPage = 1
            }
            binding.viewPagerBanners.setCurrentItem(currentPage++, true)
        }
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, delayTime, periodTime)
    }

    private fun stopBannerSlideShow() {
        timer?.cancel()
    }

    override fun onStop() {
        super.onStop()
        firstTime=true
    }

    override fun onResume() {
        super.onResume()
        firstTime=true
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }
    /**
     * This is used to update the ui in recycler view as we are not observing change in
     */
    private fun updateUserAdapterItem(item: ProductEntity, position: Int) {
        (binding.rvProduct.adapter as UserAdapter).updateItem(item, position)
    }

    /**
     * This is called when we click on add to cart button in recycler view
     */
    override fun onAddToCartClick(item: ProductEntity, position: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            showProgress()
            item.count=1
            updateUserAdapterItem(item, position)
            viewModel.inertItemToCart(item)
            delay(1000)
            hideProgress()
        }

    }

    override fun onIncreaseItemClick(item: ProductEntity, position: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            showProgress()
            item.count++
            updateUserAdapterItem(item, position)
            viewModel.updateItemToCart(item)
            delay(1000)
            hideProgress()
        }
    }

    override fun onDecreaseItemClick(item: ProductEntity, position: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            showProgress()
            if (item.count == 0) {
                hideProgress()
                return@launch
            }
            item.count--
            updateUserAdapterItem(item, position)
            viewModel.updateItemToCart(item)
            delay(1000)
            hideProgress()
        }
    }

    override fun onItemClick(productName: String) {
        startActivity(Intent(requireContext(), CategoryActivity::class.java).apply {
            this.putExtra(CategoryActivity.CATEGORY_NAME, productName)
        })
    }

}
