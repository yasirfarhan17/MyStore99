package com.noor.mystore99.amigrate.util.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log

object Util {
    fun String.decodeToBitmap(resize: Int = 80): Bitmap? {
        val decodedString: ByteArray =
            Base64.decode(this, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            .getResizedBitmap(resize)
        Log.d("SAHIL_BITMAP", "output ${bitmap?.width} ${bitmap?.height}")
        return bitmap
    }

    private fun Bitmap.getResizedBitmap(maxSize: Int): Bitmap? {
        var width = this.width
        var height = this.height
        Log.d("SAHIL_BITMAP", "input $width $height")
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(this, width, height, true)
    }
}