package com.dam.echirinosv3.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "eChirinosDS"
)

object AppDatastore {
    fun getDataStore(context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}




