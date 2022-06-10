package com.example.networkmodule.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream


object Util {
    fun String.decodeToBitmap(resize: Int): Bitmap? {
        val decodedString: ByteArray = Base64.decode(this, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            .getResizedBitmap(resize)
        Log.d("SAHIL_BITMAP", "output ${bitmap?.width} ${bitmap?.height}")
        return bitmap
    }

    fun String.decodeToBitmap(): Bitmap? {
        val decodedString: ByteArray = Base64.decode(this, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        Log.d("CATEGORY", bitmap.height.toString() + "--" + bitmap.width.toString())
        return bitmap
    }

    private fun Bitmap.getResizedBitmap(maxSize: Int): Bitmap? {
        var width = this.width
        var height = this.height
        Log.d("SAHIL_BITMAP", "input $width $height")
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            if (width > maxSize) {
                width = maxSize
            }
            height = (width / bitmapRatio).toInt()
        } else {
            if (height > maxSize) {
                height = maxSize
            }
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(this, width, height, true)
    }

    fun Bitmap.bitMapToString(): String {
        val byteArrayOutStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutStream)
        val b = byteArrayOutStream.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun String.reduceBase64ImageSize(resize: Int): String? {
        return this.decodeToBitmap(resize)?.bitMapToString()
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }
}