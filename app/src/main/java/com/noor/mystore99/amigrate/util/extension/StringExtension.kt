package com.noor.mystore99.amigrate.util.extension

object StringExtension {
    fun String.isValidPhoneNumber(): Boolean {
        return this.matches(Regex("^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}$"))
    }

}