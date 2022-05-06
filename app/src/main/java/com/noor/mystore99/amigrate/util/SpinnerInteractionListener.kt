package com.noor.mystore99.amigrate.util

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView

open class SpinnerInteractionListener : AdapterView.OnItemSelectedListener,
    View.OnTouchListener {
    var userSelect = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        userSelect = true
        return false
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View, pos: Int, id: Long) {
        if (userSelect.not()) {
            return
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}