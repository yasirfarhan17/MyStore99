package com.noor.mystore99.amigrate.ui.main.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.networkmodule.database.dao.CartDao
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.model.SliderModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.storage.PrefsUtil
import com.example.networkmodule.usecase.FirebaseGetProductUseCase
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseFragment
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.ui.category.CategoryActivity
import com.noor.mystore99.amigrate.ui.main.MainActivity
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.CategoryAdapter
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.CategoryAdapterCallback
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.UserAdapter
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.UserAdapterCallBack
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.amigrate.util.toLiveData
import com.noor.mystore99.databinding.UserFragmentBinding
import com.noor.mystore99.sliderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class UserFragment : BaseFragment<UserFragmentBinding, UserViewModel>(), UserAdapterCallBack,
    CategoryAdapterCallback {


    override val viewModel: UserViewModel by viewModels()
    override fun getViewModelClass(): Class<UserViewModel> = UserViewModel::class.java
    override fun getLayout(): Int = R.layout.user_fragment

    @Inject
    lateinit var cartdao: CartDao
    @Inject
    lateinit var productUseCase: FirebaseGetProductUseCase

    var sliderModelList = ArrayList<SliderModel>()
    private var currentPage = 2
    private var timer: Timer? = null
    private val delayTime: Long = 3000
    private val periodTime: Long = 3000
    @Inject
    lateinit var prefsUtil: PrefsUtil
    private val handler: Handler = Handler()
    protected val _viewState = MutableLiveData<ViewState>(ViewState.Idle)
    val viewState = _viewState.toLiveData()
    private var _productList = MutableLiveData<java.util.ArrayList<ProductModel>>()
    val productList = _productList.toLiveData()
    val currentDate1 = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    val currentTime1 = SimpleDateFormat("HHmmss", Locale.getDefault()).format(Date())
      var flag:Boolean=false
       var itemCount:Int=0


    private val addItemRunnable: Runnable = object : Runnable {
        override fun run() {

            if(prefsUtil.date==null){
                val calendar  =Calendar.getInstance()
                val time=calendar.timeInMillis
                //_viewState.postValue(ViewState.Loading)
                viewModel.getAllProducts()
                Toast.makeText(activity,"adding value to Db from Null",Toast.LENGTH_SHORT).show()
                prefsUtil.date=currentDate1.toString()
                prefsUtil.time=currentTime1.toString()
                flag=true
                Log.d("productcount","insidenull")
            }
            else if(currentDate1.toLong()> prefsUtil.date?.toLong()!!){
                val calendar  =Calendar.getInstance()
                val time=calendar.timeInMillis
                //_viewState.postValue(ViewState.Loading)
                viewModel.getAllProducts()
                Toast.makeText(activity,"adding value to Db from Null",Toast.LENGTH_SHORT).show()
                prefsUtil.date=currentDate1.toString()
                prefsUtil.time=currentTime1.toString()
            }
            handler.postDelayed(this, 10000)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root
        addItemRunnable.run()
        init()
        addListener()
       // viewModel.getProductFromDB()

    }
    private fun addListener() {
//        binding.searchView.setOnQueryTextFocusChangeListener { view, b ->
//            binding.labelPromoOffer.setVisible(b)
//            binding.rvCategory.setVisible(b)
//            binding.labelCategory.setVisible(b)
//            binding.viewPagerBanners.setVisible(b)
//        }



        with(binding){
            txtInputUserId.setEndIconOnClickListener {
                labelPromoOffer.visibility=View.VISIBLE
                rvCategory.visibility=View.VISIBLE
                labelCategory.visibility=View.VISIBLE
                viewPagerBanners.visibility=View.VISIBLE
                searchView.text?.clear()
                //addObservers()
            }

        }


        binding.searchView.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                viewModel.productList.observe(viewLifecycleOwner) {
                    //val list = it.map { productModel -> productModel.toProductEntity() }
                    val localList=ArrayList<ProductEntity>()
                    var flagNotFound=false
                    if(p0 ==null || p0.isEmpty()) {
                        localList.addAll(it)
                        flagNotFound=true
                        binding.labelPromoOffer.visibility=View.VISIBLE
                        binding.rvCategory.visibility=View.VISIBLE
                        binding.labelCategory.visibility=View.VISIBLE
                        binding.viewPagerBanners.visibility=View.VISIBLE

                    }
                    else{
                        for(item in it){
                            if(item.products_name.lowercase(Locale.ENGLISH).contains(p0.toString().lowercase(
                                    Locale.ENGLISH))) {
                                localList.add(item)
                                flagNotFound=true

                            }
                        }

                        if(!flagNotFound) {
                            //localList.addAll(it)
                            Toast.makeText(activity,"No Item Found",Toast.LENGTH_SHORT).show()
                        }
                        binding.labelPromoOffer.visibility=View.GONE
                        binding.rvCategory.visibility=View.GONE
                        binding.labelCategory.visibility=View.GONE
                        binding.viewPagerBanners.visibility=View.GONE
                    }

                    (binding.rvProduct.adapter as UserAdapter).submitList(
                        localList,
                        viewModel
                    )
                }
                //(binding.rvProduct.adapter as UserAdapter).filter.filter(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
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

    override fun onPause() {
        super.onPause()

        // Save pause time so items can be added on resume.


        // Cancel handler callback to add item.
        handler.removeCallbacks(addItemRunnable)
    }

    override fun addObservers() {

        viewModel.productList.observe(viewLifecycleOwner) {
            //val list = it.map { productModel -> productModel.toProductEntity() }
            if(it == null || it.isEmpty()){
                _viewState.postValue(ViewState.Loading)
            }
            else{
                (binding.rvProduct.adapter as UserAdapter).submitList(
                    it,
                    viewModel
                )
                _viewState.postValue(ViewState.Success())
                Log.d("productcount","insideobserver $it")
            }

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

    override fun onItemClick(cartEntity: CartEntity) {
        viewModel.insertToCartDb(cartEntity)
    }

    override fun searchInCartDB(id: String) :Boolean {
//        val falg=false
//        GlobalScope.launch(Dispatchers.IO) {
//                val list=cartdao.searchCartProduct(id)
//                Log.d("checkList",""+list)
//               // falg = list.products_name.equals(id)
//        }
        return false
    }

    override fun onDestroyView() {
        timer?.cancel()
        super.onDestroyView()
    }

    override fun onItemClick(productName: String) {
        startActivity(Intent(requireContext(), CategoryActivity::class.java).apply {
            this.putExtra(CategoryActivity.CATEGORY_NAME, productName)
        })
    }



}
