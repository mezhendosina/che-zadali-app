package com.che.zadali.sgoapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.che.zadali.sgoapp.R
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

        val THEME: Preferences.Key<String> = stringPreferencesKey("THEME")
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


    suspend fun saveAll(loginData: LoginData) {
        context.dataStore.edit { prefs ->
            prefs[THEME] = context.getString(R.string.same_as_system)
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
        val theme = context.dataStore.data.map {
            return@map it[THEME] ?: context.getString(R.string.same_as_system)
        }
        return SettingsData(
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
            theme.first()
        )
    }

    suspend fun deleteAll() {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN] = false
        }
    }
}

sealed class ThemeState(val name: String, stringId: Int) {
    object DarkMode : ThemeState("darkMode", R.string.dark_theme)
    object LightMode : ThemeState("Light", R.string.light_theme)
    object SameAsSystem : ThemeState("system", R.string.same_as_system)
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
    var theme: String?
)
