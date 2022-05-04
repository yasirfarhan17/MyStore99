package com.noor.mystore99.amigrate.ui.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseFragment
import com.noor.mystore99.amigrate.ui.main.MainViewModel
import com.noor.mystore99.amigrate.util.extension.obtainViewModel
import com.noor.mystore99.databinding.UserFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@AndroidEntryPoint
class UserFragment : BaseFragment<UserFragmentBinding, UserViewModel>() {
    val mainViewModel: MainViewModel by activityViewModels()
    override val viewModel: UserViewModel by activityViewModels()

    @FlowPreview
    private val homeViewModel: MainViewModel by lazy {
        obtainViewModel(
            requireActivity(),
            MainViewModel::class.java,
            defaultViewModelProviderFactory
        )
    }






    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root


        addObservers()
        init()
    }

    override fun getViewModelClass(): Class<UserViewModel> = UserViewModel::class.java

    override fun getLayout(): Int = R.layout.user_fragment

    private fun init(){
        with(binding) {

            productRecyclerView1.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            //shimmer.visibility = View.VISIBLE
            mainViewModel.getProductFromDB()
            productRecyclerView1.adapter = UserAdapter(this@UserFragment)
        }
    }

    override fun addObservers() {
        mainViewModel.productFromDB.observe(viewLifecycleOwner) {
            (binding.productRecyclerView1.adapter as UserAdapter).submitList(it)
            Log.d("yasir check",""+it)
        }
    }

}


/*class UserFragment : Fragment() {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        // TODO: Use the ViewModel
    }

}*/