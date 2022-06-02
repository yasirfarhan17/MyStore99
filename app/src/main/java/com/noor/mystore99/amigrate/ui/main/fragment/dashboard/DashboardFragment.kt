package com.noor.mystore99.amigrate.ui.main.fragment.dashboard

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networkmodule.model.DashBoardModel
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.ui.dashboard.account.address.Address
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.CategoryAdapter
import com.noor.mystore99.databinding.ActivityMain2Binding

class DashboardFragment : Fragment() {

    private var _binding: ActivityMain2Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val arr=ArrayList<DashBoardModel>()

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
        binding.rvDash.adapter=DashBoardAdapter()
        (binding.rvDash.adapter as DashBoardAdapter).submitList(arr)

        //setSingleEvent(binding.grid)
        return binding.root
    }




    private fun setSingleEvent(mGrid: GridLayout) {
        for (i in 0 until mGrid.childCount) {
            val cardView = mGrid.getChildAt(i) as CardView
            cardView.setOnClickListener {
                if (cardView.cardBackgroundColor.defaultColor == -1) {
                    cardView.setCardBackgroundColor(Color.parseColor("#41CEFC"))
                    //Toast.makeText(Visitor_Home.this,""+final1,Toast.LENGTH_LONG).show();
                } else {
                    cardView.setCardBackgroundColor(Color.parseColor("#41CEFC"))
                }
                if (i == 0) {
                    val intent = Intent(activity, Address::class.java)
                    //intent.putExtra("id",userId);
                    startActivity(intent)
                }
                if (i == 1) {
                    //val intent = Intent(this@MainActivity, product1::class.java)
                    //intent.putExtra("id",userId);
                    //startActivity(intent)
                }
                if (i == 2) {
                    //val intent = Intent(this@MainActivity, OrderHomecheck::class.java)
                    //intent.putExtra("id",userId);
                   // startActivity(intent)
                }
                if (i == 3) {
                    //val intent = Intent(this@MainActivity, BannerHome::class.java)
                    //intent.putExtra("id",userId);
                   // startActivity(intent)
                }
                if (i == 4) {
                    //val intent = Intent(this@MainActivity, User::class.java)
                    //intent.putExtra("id",userId);
                   // startActivity(intent)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}