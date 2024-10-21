package com.dam.echirinosv3.data.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.dam.echirinosv3.data.model.Preferencias
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class MainRepository(private val context: Context, private val dataStore: DataStore<Preferences>) {

    private companion object {
        val DEFAULT_TIME_SPLASH = intPreferencesKey("default_time_splash")
        val SHOW_LOGIN_ONSTART = booleanPreferencesKey("show_login_onstart")

    }


    fun getPreferences(): Flow<Preferencias> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            Preferencias(
                showLoginOnStart = it[SHOW_LOGIN_ONSTART] ?: true,
                defaultTimeSplash = it[DEFAULT_TIME_SPLASH] ?: 3
            )
        }


    suspend fun savePreferences(prefs: Preferencias) {
        dataStore.edit { preferences ->
            preferences[SHOW_LOGIN_ONSTART] = prefs.showLoginOnStart
            preferences[DEFAULT_TIME_SPLASH] = prefs.defaultTimeSplash
        }
    }
}
