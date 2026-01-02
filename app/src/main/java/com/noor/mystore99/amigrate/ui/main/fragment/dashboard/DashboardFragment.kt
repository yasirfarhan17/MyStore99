package com.noor.mystore99.amigrate.ui.main.fragment.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.model.DashBoardModel
import com.example.networkmodule.storage.PrefsUtil
import com.noor.mystore99.AboutPage
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.ui.auth.login.LoginActivity
import com.noor.mystore99.amigrate.ui.dashboard.account.myorder.MyOrder
import com.noor.mystore99.amigrate.ui.dashboard.account.profile.ProfileActivity
import com.noor.mystore99.amigrate.ui.payment.PaymentViewModel
import com.noor.mystore99.databinding.ActivityMain2Binding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment(), DashBoardCallBack {

    private var _binding: ActivityMain2Binding? = null
    private val binding get() = _binding!!
    
    private val paymentViewModel: PaymentViewModel by viewModels()
    private val menuItems = ArrayList<DashBoardModel>()
    
    @Inject
    lateinit var prefsUtil: PrefsUtil
    
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = prefsUtil.Name.toString()
        paymentViewModel.getUserDet(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityMain2Binding.inflate(inflater, container, false)
        
        setupMenuItems()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        return binding.root
    }

    private fun setupMenuItems() {
        menuItems.clear()
        menuItems.add(DashBoardModel(R.drawable.action_user, "My Account"))
        menuItems.add(DashBoardModel(R.drawable.ic_local_mall_black_24dp, "My Order"))
        menuItems.add(DashBoardModel(R.drawable.about, "About us"))
        menuItems.add(DashBoardModel(R.drawable.ic_signout, "Log Out"))
    }

    private fun setupRecyclerView() {
        binding.rvDash.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DashBoardAdapter(this@DashboardFragment).apply {
                submitList(menuItems)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        paymentViewModel.userDetail.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.tvName.text = "Hi ${it.name}"
                
                it.photo?.let { photoData ->
                    if (photoData.isNotBlank()) {
                        // Handle both URL (new) and Base64 (legacy) formats
                        if (photoData.startsWith("http")) {
                            // New format: Firebase Storage URL
                            binding.imageProfile.load(photoData) {
                                crossfade(true)
                                transformations(CircleCropTransformation())
                                placeholder(R.drawable.usericon)
                                error(R.drawable.usericon)
                            }
                        } else {
                            // Legacy format: Base64 encoded image
                            try {
                                val decodedString = Base64.decode(photoData, Base64.DEFAULT)
                                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                                binding.imageProfile.load(decodedByte) {
                                    transformations(CircleCropTransformation())
                                    placeholder(R.drawable.usericon)
                                    error(R.drawable.usericon)
                                }
                            } catch (e: Exception) {
                                binding.imageProfile.setImageResource(R.drawable.usericon)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        // Allow tapping profile card to navigate to profile
        binding.cvProfile.setOnClickListener {
            startActivity(Intent(activity, ProfileActivity::class.java))
        }
        
        // Setup swipe-to-refresh
        binding.swipeRefresh.setOnRefreshListener {
            refreshUserData()
        }
    }
    
    private fun refreshUserData() {
        paymentViewModel.getUserDet(userId)
        // Stop refreshing after a short delay (data will update via observer)
        binding.swipeRefresh.postDelayed({
            binding.swipeRefresh.isRefreshing = false
        }, 1000)
    }

    override fun onItemClick(productName: String) {
        when (productName) {
            "My Order" -> startActivity(Intent(activity, MyOrder::class.java))
            "My Account" -> startActivity(Intent(activity, ProfileActivity::class.java))
            "About us" -> startActivity(Intent(activity, AboutPage::class.java))
            "Log Out" -> showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        context?.let { ctx ->
            AlertDialog.Builder(ctx)
                .setTitle("Exit")
                .setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    performLogout()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
                .create()
                .show()
        }
    }
    
    private fun performLogout() {
        // Fade out animation
        binding.root.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                prefsUtil.Name = ""
                prefsUtil.password = ""
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
            .start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}