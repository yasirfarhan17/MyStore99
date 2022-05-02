package com.noor.mystore99.amigrate.ui.main.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseFragment
import com.noor.mystore99.amigrate.ui.main.MainViewModel
import com.noor.mystore99.amigrate.util.extension.obtainViewModel
import com.noor.mystore99.databinding.UserFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@AndroidEntryPoint
class UserFragment : BaseFragment<UserFragmentBinding, UserViewModel>() {

    @FlowPreview
    private val homeViewModel: MainViewModel by lazy {
        obtainViewModel(
            requireActivity(),
            MainViewModel::class.java,
            defaultViewModelProviderFactory
        )
    }

    val mainViewModel: MainViewModel by activityViewModels()
    override val viewModel: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root
    }

    override fun getViewModelClass(): Class<UserViewModel> = UserViewModel::class.java

    override fun getLayout(): Int = R.layout.user_fragment

    override fun addObservers() {
        viewModel.bannerList.observe(this) {
            binding.tvUserViewmodel.text = it.toString()
        }
        mainViewModel.productList.observe(this) {
            binding.tvMainViewmodel.text = it.toString()
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