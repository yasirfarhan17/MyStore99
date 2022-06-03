package com.noor.mystore99.amigrate.util.extension

import android.graphics.Bitmap
import android.widget.ImageView
import coil.load

object CoilExtension {
    fun ImageView.loadBitmap(bitmap: Bitmap) {
        this.load(bitmap)
    }

}