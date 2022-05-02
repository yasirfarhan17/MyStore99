package com.noor.mystore99.amigrate.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.noor.mystore99.amigrate.util.UiUtil

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    protected lateinit var binding: B
    protected abstract val viewModel: VM
    protected lateinit var uiUtil: UiUtil

    /*  @Inject
      lateinit var prefsUtil: PrefsUtil
  */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiUtil = UiUtil(requireContext())
        observeViewState()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
    }

    private fun observeViewState() {
        viewModel.viewState
            .observe(this) {
                when (it) {
                    ViewState.Loading -> {
                        showProgress()
                    }
                    is ViewState.Success -> {
                        hideProgress()
                        showMessage(it.message)
                    }
                    is ViewState.Error -> {
                        hideProgress()
                        handleException(it.throwable)
                    }
                    else -> Log.d("onserveState", it.toString())
                }
            }
    }

    private fun handleException(throwable: Throwable?) {
        showMessage(throwable?.message)
    }


    protected fun showProgress() {
        uiUtil.showProgress()
    }

    protected fun hideProgress() {
        uiUtil.hideProgress()
    }

    protected fun showMessage(message: String?) {
        message?.let { uiUtil.showMessage(it) }
    }

    protected fun showToast(
        message: String?,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        message?.let {
            Toast.makeText(requireContext(), it, duration)
                .show()
        }
    }


    abstract fun getViewModelClass(): Class<VM>

    @LayoutRes
    abstract fun getLayout(): Int


    abstract fun addObservers()
}

/*
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    protected lateinit var binding: VB private set
    protected lateinit var viewModel: VM private set

    private val type = (javaClass.genericSuperclass as ParameterizedType)
    private val classVB = type.actualTypeArguments[0] as Class<VB>
    private val classVM = type.actualTypeArguments[1] as Class<VM>

    private val inflateMethod = classVB.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = inflateMethod.invoke(null, inflater, container, false) as VB
        viewModel = createViewModelLazy(classVM.kotlin, { viewModelStore }).value
        return binding.root
    }
}*/
