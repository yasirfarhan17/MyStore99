package com.noor.mystore99.amigrate.util

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnEnd
import com.noor.mystore99.R

object Util {

    fun View.setVisible(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun Context.flipCard(visibleView: View, inVisibleView: View, error: (String) -> Unit) {
        try {
            visibleView.setVisible(true)
            val scale = theme.resources.displayMetrics.density
            val cameraDist = 10000 * scale
            visibleView.cameraDistance = cameraDist
            inVisibleView.cameraDistance = cameraDist
            val flipOutAnimatorSet = AnimatorInflater.loadAnimator(this, R.animator.flip_in) as AnimatorSet
            flipOutAnimatorSet.setTarget(inVisibleView)
            val flipInAnimatorSet = AnimatorInflater.loadAnimator(this, R.animator.flip_out) as AnimatorSet
            flipInAnimatorSet.setTarget(visibleView)
            flipOutAnimatorSet.start()
            flipInAnimatorSet.start()
            flipInAnimatorSet.doOnEnd {
                inVisibleView.setVisible(false)
            }
        } catch (e: Exception) {
            error.invoke(e.message.toString())
        }
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