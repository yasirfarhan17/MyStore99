package com.noor.mystore99.amigrate.ui.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.FirebaseGetProductUseCase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.alarmManager.SetFirebaseDataReciver
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.ui.cart.CartActivity
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserViewModel
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.amigrate.util.toLiveData
import com.noor.mystore99.databinding.ActivityMain3Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity  : BaseActivity<ActivityMain3Binding, MainViewModel>() {


    override val viewModel: MainViewModel by viewModels()
     val userModelModel: UserViewModel by viewModels()
    val currentDate1 = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    val currentTime1 = SimpleDateFormat("HHmmss", Locale.getDefault()).format(Date())
    companion object{
        var prductCount=0
    }


    @Inject
    lateinit var productUseCase: FirebaseGetProductUseCase

    override fun layoutId(): Int = R.layout.activity_main3


    private val PREF_PAUSE_TIME_KEY = "exit_time"

    private val MILLIS_IN_DAY = 86400000L

    private val TRIGGER_HOUR = 11
    private val TRIGGER_MIN = 60000L
    private val TRIGGER_SEC = 0

    private val handler: Handler = Handler()
    private var _productList = MutableLiveData<ArrayList<ProductModel>>()
    val productList = _productList.toLiveData()
    protected val _viewState = MutableLiveData<ViewState>(ViewState.Idle)
    val viewState = _viewState.toLiveData()

    private val calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavView()

        val calendar  =Calendar.getInstance()
        val time=calendar.timeInMillis
//        val timeHours= abs(currentTime1.toInt() - 222500)20220529
//        val time : Long=((timeHours*3600)*1000).toLong()
        val timeTenSeconds=1000*10
        //addItemRunnable.run()
        Log.d("checkingTime", "${prefsUtil.date} ${prefsUtil.time}")
       // insertDataToFirebase((time+timeTenSeconds))

    }
    override fun onPause() {
        super.onPause()

        // Save pause time so items can be added on resume.


        // Cancel handler callback to add item.
        //handler.removeCallbacks(addItemRunnable)
    }

    private fun setNavView() {
        val navView: BottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        binding.fabBtCart.setOnClickListener {
            val intent=Intent(this,CartActivity::class.java)
            startActivity(intent)
        }
    }


    override fun addObservers() {
        lifecycleScope.launch {
            viewModel.cartItemCount.collect {
                Log.d("SAHIL", it.toString())
                if (it == 0) {
                    binding.clBatch.setVisible(false)
                    return@collect
                }
                binding.clBatch.setVisible(true)
                binding.tvBatch.text = it.toString()
            }

        }
    }

    fun insertDataToFirebase(timeInMilli:Long){
        val alarmManager :AlarmManager= getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent=Intent(this,SetFirebaseDataReciver::class.java)
        val pendingIntent=PendingIntent.getBroadcast(this,0,intent,0)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,timeInMilli,AlarmManager.INTERVAL_DAY,pendingIntent)
        //snacToast.makeText(this,"inside Alarm manager",Toast.LENGTH_SHORT).show()
    }



}