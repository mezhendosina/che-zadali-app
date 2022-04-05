package com.che.zadali.sgoapp.data.services

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.data.LoginData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsPrefs(private val context: Context) {
    companion object {
        val LOGGED_IN: Preferences.Key<Boolean> = booleanPreferencesKey("LOGGED_IN")

        val LOGIN: Preferences.Key<String> = stringPreferencesKey("LOGIN")
        val PASSWORD: Preferences.Key<String> = stringPreferencesKey("PASSWORD")
        val SCHOOL_ID: Preferences.Key<Int> = intPreferencesKey("SCHOOL_ID")
        val CITY_ID: Preferences.Key<Int> = intPreferencesKey("CITY_ID")
        val PROVINCE_ID: Preferences.Key<Int> = intPreferencesKey("PROVINCE_ID")

        val LANGUAGE: Preferences.Key<String> = stringPreferencesKey("LANGUAGE")

        val THEME1: Preferences.Key<Int> = intPreferencesKey("THEME1")
    }

    val loggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[LOGGED_IN] ?: false
    }
    val login: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[LOGIN] ?: "login"
    }

    val language: Flow<String> = context.dataStore.data.map {
        it[LANGUAGE] ?: ""
    }

    val theme: Flow<Int> =
        context.dataStore.data.map { it[THEME1] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM }

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

    suspend fun loadSettingsData(): SettingsData {
        //TODO net code
        val login = context.dataStore.data.map { return@map it[LOGIN] }
        val data = SettingsData(
            login.first(),
            false,
            "+123456789",
            true,
            "mensh.zheya@gmail.com",
            true,
            "Евгений",
            "Меньшенин",
            "Андреевич",
            false,
            "22.09.2005",
            false,
            "2021/2022",
            listOf("2021/2022", "2020/2021"),
            "Русский",
            listOf("Русский"),
            when (theme.first()) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> R.string.same_as_system
                AppCompatDelegate.MODE_NIGHT_NO -> R.string.light_theme
                AppCompatDelegate.MODE_NIGHT_YES -> R.string.dark_theme
                else -> 0
            }
        )

        return data
    }

    suspend fun deleteAll() {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN] = false
        }
    }
    suspend fun editTheme(theme: Int){
        context.dataStore.edit {
            it[THEME1] = theme
        }
    }
}


data class SettingsData(
    var login: String?,
    val editableLogin: Boolean,

    var phoneNum: String,
    val editablePhoneNum: Boolean,

    var email: String,
    val editableEmail: Boolean,

    var firstName: String,
    var lastName: String,
    var thirdName: String,
    val editableName: Boolean,

    var birthday: String,
    val editableBirthday: Boolean,

    var current_year: String,
    val years: List<String>,
    var current_language: String,
    val languages: List<String>,
    val theme: Int
)
