package com.noor.mystore99.amigrate.ui.main.fragment.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.SliderModel
import com.google.firebase.database.*
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseFragment
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.CategoryAdapter
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.UserAdapter
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.databinding.UserFragmentBinding
import com.noor.mystore99.sliderAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class UserFragment : BaseFragment<UserFragmentBinding, UserViewModel>() {
    override val viewModel: UserViewModel by viewModels()
    override fun getViewModelClass(): Class<UserViewModel> = UserViewModel::class.java
    override fun getLayout(): Int = R.layout.user_fragment
    lateinit var ref:DatabaseReference
    var sliderModelList=ArrayList<SliderModel>()
    private var currentPage=2
    private var timer: Timer? = null
    private val delayTime: Long = 3000
    private val periodTime: Long = 3000
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root
        init()
        setBanner()
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
            if (it.isNullOrEmpty()) {
                binding.labelCategory.setVisible(false)
                binding.rvCategory.setVisible(false)
                return@observe
            }
            binding.labelCategory.setVisible(true)
            binding.rvCategory.setVisible(true)
            (binding.rvCategory.adapter as CategoryAdapter).submitList(it as ArrayList<CategoryModel>)
        }

    }

    fun setBanner(){
        ref = FirebaseDatabase.getInstance().getReference("Banner")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val count = dataSnapshot.childrenCount.toInt()
                    for (i in 0 until count + 1) {
                        for (dataSnapshot1 in dataSnapshot.children) {
                            val val1 = dataSnapshot1.child("img").value.toString()
                            val color = dataSnapshot1.child("color").value.toString()
                            val ob = SliderModel(val1, color)
                            sliderModelList.add(ob)
                        }
                    }
                    val sliderAdapter = sliderAdapter(sliderModelList)
                    binding.viewPagerBanners.setAdapter(sliderAdapter)
                    binding.viewPagerBanners.setClipToPadding(false)
                    binding.viewPagerBanners.setPageMargin(20)
                    sliderAdapter.notifyDataSetChanged()
                    binding.viewPagerBanners.setCurrentItem(currentPage)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


        val onPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

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

        startbannerslideshow()
        binding.viewPagerBanners.setOnTouchListener(OnTouchListener { view, motionEvent ->
            pageLooper()
            stopbannerslidshow()
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                startbannerslideshow()
            }
            false
        })
    }


    /////banner slider
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

    private fun startbannerslideshow() {
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

    private fun stopbannerslidshow() {
        timer?.cancel()
    }

}
