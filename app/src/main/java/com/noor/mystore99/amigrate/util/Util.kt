package com.noor.mystore99.amigrate.util

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.noor.mystore99.R

object Util {

    fun View.setVisible(visible:Boolean){
        this.visibility=if(visible) View.VISIBLE else View.GONE
    }
    fun showAlert(
        context: Context?,
        title: String?,
        message: String,
        clearCart: () -> Unit
    ) {
        if (context == null) return
        val builder1 = AlertDialog.Builder(context)
        builder1.setTitle(title)
        builder1.setMessage(message)
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            R.string.yes
        ) { dialog: DialogInterface, id: Int ->
            dialog.cancel()
            clearCart.invoke()
        }
        builder1.setNegativeButton(
            R.string.cancel
        ) { dialog: DialogInterface, id: Int ->
            dialog.cancel()
        }
        val alert11 = builder1.create()
        alert11.setOnShowListener {
            val themeButtonColor = "#000000"
            if (themeButtonColor.isNotEmpty()) {
                alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor(themeButtonColor))
                alert11.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(Color.parseColor(themeButtonColor))
            }
        }
        alert11.setCanceledOnTouchOutside(false)
        alert11.show()
    }

    fun freeMemory() {
        try {
            System.runFinalization()
            Runtime.getRuntime().gc()
            System.gc()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}