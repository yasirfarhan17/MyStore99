package com.noor.mystore99.amigrate.util


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.noor.mystore99.R
import com.noor.mystore99.databinding.LayoutDialogBinding


class ProgressDialog(context: Context) : Dialog(context) {

    private lateinit var binding: LayoutDialogBinding
    override fun onStart() {
        val dialog: Dialog = this
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window?.setLayout(width, height)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_dialog,
            null, false
        )
        setContentView(binding.root)
    }

}