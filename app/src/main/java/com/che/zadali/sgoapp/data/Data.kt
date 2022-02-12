package com.che.zadali.sgoapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//App settings
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsPrefs(private val context: Context) {
    companion object {
        val LOGGED_IN: Preferences.Key<Boolean> = booleanPreferencesKey("LOGGED_IN")

        val LOGIN: Preferences.Key<String> = stringPreferencesKey("LOGIN")
        val PASSWORD: Preferences.Key<String> = stringPreferencesKey("PASSWORD")
        val SCHOOL_ID: Preferences.Key<Int> = intPreferencesKey("SCHOOL_ID")
        val CITY_ID: Preferences.Key<Int> = intPreferencesKey("CITY_ID")
        val PROVINCE_ID: Preferences.Key<Int> = intPreferencesKey("PROVINCE_ID")

        val THEME: Preferences.Key<String> = stringPreferencesKey("THEME")
    }

    val loggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[LOGGED_IN] ?: false
    }

    val login: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[LOGIN] ?: "login"
    }

    suspend fun saveAll(loginData: LoginData) {
        context.dataStore.edit { prefs ->
            prefs[LOGIN] = loginData.login
            prefs[PASSWORD] = loginData.password
            prefs[SCHOOL_ID] = loginData.school_id
            prefs[CITY_ID] = loginData.city_id
            prefs[PROVINCE_ID] = loginData.province_id
            prefs[LOGGED_IN] = true
        }
    }

    suspend fun changePassword(newPassword: String) {
        context.dataStore.edit { prefs ->
            prefs[PASSWORD] = newPassword
        }
    }

    suspend fun deleteAll(){
        context.dataStore.edit { prefs ->
            prefs[LOGIN] = ""
            prefs[PASSWORD] = ""
            prefs[SCHOOL_ID] = 0
            prefs[CITY_ID] = 0
            prefs[PROVINCE_ID] = 0
            prefs[LOGGED_IN] = false
        }
    }

}

//Data class for send request to server
data class LoginData(
    val login: String,
    val password: String,
    val school_id: Int,
    val city_id: Int,
    val province_id: Int
)

//Output
data class Output(
    val output: String,
    val loggedIn: Boolean
)

