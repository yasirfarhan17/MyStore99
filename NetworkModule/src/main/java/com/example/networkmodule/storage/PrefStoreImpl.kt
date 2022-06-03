package com.example.networkmodule.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


const val DataStore_NAME = "SABZI_TAZA_DATA_STORE"
val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DataStore_NAME)

class PrefsStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PrefStore {

    companion object {
        val NAME = stringPreferencesKey("NAME")
        val PHONE_NUMBER = stringPreferencesKey("PHONE")
        val PASSWORD = stringPreferencesKey("PASSWORD")
        val LOGGED_IN = booleanPreferencesKey("LOGGED_IN")
    }


    override fun getPhoneNumber(): Flow<String> =
        context.datastore.data.map { it -> it[PHONE_NUMBER] ?: "" }

    override suspend fun setPhoneNumber(phoneNumber: String) {
        context.datastore.edit {
            it[PHONE_NUMBER] = phoneNumber
        }
    }

    override fun getPassword(): Flow<String?> = context.datastore.data.map { it[PASSWORD] }
    override suspend fun setPassword(password: String) {
        context.datastore.edit {
            it[PASSWORD] = password
        }
    }

    override fun isLoggedIn(): Flow<Boolean> = context.datastore.data.map { it[LOGGED_IN] ?: false }
    override suspend fun setLoggedIn(loggedIn: Boolean) {
        context.datastore.edit {
            it[LOGGED_IN] = loggedIn
        }
    }
}