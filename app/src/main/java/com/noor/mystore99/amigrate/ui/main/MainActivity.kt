package com.noor.mystore99.amigrate.ui.main

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.networkmodule.usecase.FirebaseGetProductUseCase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.BuildConfig
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.ui.cart.CartActivity
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserViewModel
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.amigrate.util.toLiveData
import com.noor.mystore99.databinding.ActivityMain3Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity  : BaseActivity<ActivityMain3Binding, MainViewModel>() {


    override val viewModel: MainViewModel by viewModels()
     val viewModel1: UserViewModel by viewModels()
    var handler = Handler()
    companion object{
        lateinit var key :String
        lateinit var count:String
    }








    @Inject
    lateinit var productUseCase: FirebaseGetProductUseCase
    var ref=FirebaseDatabase.getInstance().reference

    override fun layoutId(): Int = R.layout.activity_main3



    protected val _viewState = MutableLiveData<ViewState>(ViewState.Idle)
    val viewState = _viewState.toLiveData()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         key= prefsUtil.Name.toString()
        if(checkInternet()) {
            val versionCode = BuildConfig.VERSION_CODE
            val codee = IntArray(1)

            ref = FirebaseDatabase.getInstance().getReference("version").child("version")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("hooo", "working")
                    if (snapshot.exists()) {
                        codee[0] = snapshot.value.toString().toInt()
                    }
                    if (versionCode < codee[0]) {
                        val alertDialogBuilder = AlertDialog.Builder(
                            this@MainActivity
                        )

                        // set title
                        alertDialogBuilder.setTitle("SabziTaza Update")

                        // set dialog message
                        alertDialogBuilder
                            .setMessage("Please update SabziTaza to the latest version.")
                            .setCancelable(false)
                            .setPositiveButton(
                                "Update"
                            ) { dialog, id ->
                                // if this button is clicked, close
                                // current activity
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.noor.mystore99")
                                )
                                startActivity(intent)
                                //show_Notification("LogOut","You Log Out Your account at "+ currentTime);
                                //EmployeeHome.this.finish();
                            }
                            .setNegativeButton(
                                "Cancel"
                            ) { dialog, id -> // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel()
                            }

                        // create alert dialog
                        val alertDialog = alertDialogBuilder.create()

                        // show it
                        alertDialog.show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
            setNavView()
            onResume()
        }
        else{
            Toast.makeText(this@MainActivity, "No Internet Connection", Toast.LENGTH_LONG).show()
        }


       // insertDataToFirebase((time+timeTenSeconds))

    }

        override fun onBackPressed() {
            val alertDialogBuilder = AlertDialog.Builder(
                this@MainActivity
            )

            // set title

            // set title
            alertDialogBuilder.setTitle("Exit")

            // set dialog message

            // set dialog message
            alertDialogBuilder
                .setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // if this button is clicked, close
                    // current activity
                    val a = Intent(Intent.ACTION_MAIN)
                    a.addCategory(Intent.CATEGORY_HOME)
                    a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(a)
                    //show_Notification("LogOut","You Log Out Your account at "+ currentTime);
                    //EmployeeHome.this.finish();
                }
                .setNegativeButton("No") { dialog, id -> // if this button is clicked, just close
                    // the dialog box and do nothing
                    dialog.cancel()
                }

            // create alert dialog

            // create alert dialog
            val alertDialog = alertDialogBuilder.create()

            // show it

            // show it
            alertDialog.show()

    }

    override fun onResume() {
        super.onResume()

        val ref= key.let { FirebaseDatabase.getInstance().getReference("CartNew").child(it) }
        Log.d("InsideOnResume",key)
        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    val count=snapshot.childrenCount
                    if(count.toInt()>0){
                        Log.d("InsideOnResume",count.toString() )
                        binding.tvBatch.visibility=View.VISIBLE
                        binding.clBatch.visibility=View.VISIBLE
                        binding.tvBatch.text = count.toString()
                    }
                }
                else{
                    Log.d("InsideOnResume","no value" )
                    binding.tvBatch.visibility=View.INVISIBLE
                    binding.clBatch.visibility=View.INVISIBLE

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


            })
    }

                private fun setNavView() {
                    val navView: BottomNavigationView = binding.bottomNavigationView
                    val navController = findNavController(R.id.nav_host_fragment_activity_main)
                    navView.setupWithNavController(navController)
                    //binding.clBatch.setVisible(false)
                    binding.fabBtCart.setOnClickListener {
                        val intent = Intent(this, CartActivity::class.java)
                        startActivity(intent)
                    }
//                    navView.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
//                        override fun onNavigationItemSelected(item: MenuItem): Boolean {
//                            when(item.itemId){
//                                R.id.navigation_home->{
//                                    navController.navigate(R.id.navigation_home)
//                                    jump()
//                                    return true
//                                }
//                                R.id.navigation_user->{
//                                    navController.navigate(R.id.navigation_user)
//                                    return true
//                                }
//                            }
//                            return false
//                        }
//
//                    })


                }


                override fun addObservers() {
            lifecycleScope.launch {
                viewModel1.cartFromDB.observe(this@MainActivity){
                    if(it.size>0) {
                        val count = it.size
                        binding.clBatch.setVisible(true)
                        binding.tvBatch.text = count.toString()
                    }
                    else{
                        binding.clBatch.setVisible(false)
                    }
                }

            }
        }

    fun checkInternet(): Boolean {
        var connected = false
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connected =
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
            ) {
                //we are connected to a network
                true
            } else false
        return connected
    }




}