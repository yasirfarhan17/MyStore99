package com.noor.mystore99.amigrate.util.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object Util {
    fun String.decodeToBitmap(): Bitmap {
        val decodedString: ByteArray =
            Base64.decode(this, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
}