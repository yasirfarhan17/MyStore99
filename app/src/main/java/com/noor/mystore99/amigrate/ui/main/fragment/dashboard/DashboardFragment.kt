package com.noor.mystore99.amigrate.ui.main.fragment.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.model.DashBoardModel
import com.example.networkmodule.storage.PrefsUtil
import com.noor.mystore99.AboutPage
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.ui.auth.login.LoginActivity
import com.noor.mystore99.amigrate.ui.dashboard.account.address.AddressActivity
import com.noor.mystore99.amigrate.ui.dashboard.account.myorder.MyOrder
import com.noor.mystore99.amigrate.ui.dashboard.account.profile.ProfileActivity
import com.noor.mystore99.amigrate.ui.payment.PaymentViewModel
import com.noor.mystore99.databinding.ActivityMain2Binding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment(),DashBoardCallBack {

    private var _binding: ActivityMain2Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val payment: PaymentViewModel by viewModels()
    private val binding get() = _binding!!
    val arr=ArrayList<DashBoardModel>()
    @Inject
    lateinit var prefsUtil: PrefsUtil
    lateinit var key:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        key=prefsUtil.Name.toString()
        payment.getUserDet(key)
        addObservers()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]
        _binding = ActivityMain2Binding.inflate(inflater, container, false)
        arr.clear()
        arr.add(DashBoardModel(R.drawable.action_user,"My Account"))
        arr.add(DashBoardModel(R.drawable.ic_local_mall_black_24dp,"My Order"))
        arr.add(DashBoardModel(R.drawable.about,"About us"))
        arr.add(DashBoardModel(R.drawable.ic_signout,"Log Out"))
        binding.rvDash.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvDash.adapter=DashBoardAdapter(this)
        (binding.rvDash.adapter as DashBoardAdapter).submitList(arr)

        //setSingleEvent(binding.grid)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
     fun addObservers() {
        payment.userDetail.observe(this){
            with(binding){
                tvName.text="Hi ${it.name}"
                //tvName.setText(it.name)
                if(it.photo!=null) {
                    val `val`: String = it.photo.toString()

                    val decodedString = Base64.decode(`val`, Base64.DEFAULT)
                    val decodedByte =
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    // imageProfile.setImageBitmap(decodedByte)

                    imageProfile.load(decodedByte) {
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(productName: String) {
        if(productName.equals("My Order"))
        {
            val intent=Intent(activity,MyOrder::class.java)
            startActivity(intent)
        }
        else if(productName.equals("My Account"))
        {
            val intent=Intent(activity, ProfileActivity::class.java)
            startActivity(intent)
        }
        else if(productName.equals("About us"))
        {
            val intent=Intent(activity, AboutPage::class.java)
            startActivity(intent)
        }
        else if(productName.equals("Log Out"))
        {
            val alertDialogBuilder = context?.let {
                AlertDialog.Builder(
                    it
                )
            }

            // set title

            // set title
            alertDialogBuilder?.setTitle("Exit")

            // set dialog message

            // set dialog message
            alertDialogBuilder
                ?.setMessage("Do you really want to exit?")
                ?.setCancelable(false)
                ?.setPositiveButton("Yes") { dialog, id ->
                    // if this button is clicked, close
                    // current activity
                    //show_Notification("LogOut","You Log Out Your account at "+ currentTime);
                    //EmployeeHome.this.finish();
                    prefsUtil.Name=""
                    prefsUtil.password=""
                    startActivity(Intent(activity,LoginActivity::class.java))
                }
                ?.setNegativeButton("No") { dialog, id -> // if this button is clicked, just close
                    // the dialog box and do nothing
                    dialog.cancel()
                }

            // create alert dialog

            // create alert dialog
            val alertDialog = alertDialogBuilder?.create()

            // show it

            // show it
            alertDialog?.show()
        }
    }
}