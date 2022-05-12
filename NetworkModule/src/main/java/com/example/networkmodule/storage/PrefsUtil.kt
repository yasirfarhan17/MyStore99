package com.example.networkmodule.storage

import android.content.SharedPreferences
import androidx.core.content.edit

class PrefsUtil(
    private val pref: SharedPreferences
) {

    companion object {
        const val SHARED_PREFERENCE_ID = "SABZI_TAZA_SPF"
        private const val PHONE_NO = "PHONE_NO"
        private const val IS_LOGGED_IN = "IS_LOGGED_IN"
        private const val NAME = "NAME"
        private const val PASSWORD = "PASSWORD"
    }


    fun logout() {
        pref.edit {
            remove(PASSWORD)
            remove(IS_LOGGED_IN)
            remove(PHONE_NO)
            remove(NAME)
        }
    }


    var Name: String?
        get() = pref.getString(NAME, null)
        set(value) = pref.edit { putString(NAME, value).apply() }

    var phoneNo: String?
        get() = pref.getString(PHONE_NO, null)
        set(value) = pref.edit { putString(PHONE_NO, value).apply() }

    var password: String?
        get() = pref.getString(PASSWORD, null)
        set(value) = pref.edit { putString(PASSWORD, value) }

    var isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGGED_IN, false)
        set(value) = pref.edit { putBoolean(IS_LOGGED_IN, value) }
}



