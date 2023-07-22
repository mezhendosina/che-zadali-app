package com.mezhendosina.sgo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mezhendosina.sgo.data.netschool.api.login.LoginEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun <T> Preferences.Key<T>.getValue(context: Context, defaultValue: T): Flow<T> =
    context.dataStore.data.map { it[this] ?: defaultValue }

suspend fun <T> Preferences.Key<T>.editPreference(context: Context, value: T) =
    context.dataStore.edit {
        it[this] = value
    }

class SettingsDataStore {

    companion object {
        val REGION_URL = stringPreferencesKey("region")

        val LOGGED_IN = booleanPreferencesKey("logged_in")

        val LOGIN = stringPreferencesKey("login")
        val PASSWORD = stringPreferencesKey("password")

        val ESIA_LOGIN_STATE = stringPreferencesKey("esia_login_state")
        val ESIA_USER_ID = stringPreferencesKey("esia_user_id")

        val CURRENT_USER_ID = intPreferencesKey("current_user_id")
        val THEME = intPreferencesKey("theme")
        val CURRENT_TRIM_ID = intPreferencesKey("current_trim_id")
        val LAST_VERSION_NUMBER = intPreferencesKey("last_version_number")
        val SHOW_UPDATE_DIALOG = booleanPreferencesKey("show_update_dialog")
        val SORT_GRADES_BY = intPreferencesKey("sort_grades_by")
        val DIARY_STYLE = stringPreferencesKey("diary_style")

        val SKIP_SUNDAY = booleanPreferencesKey("skip_sunday")

        val SCHOOL_ID = intPreferencesKey("scid")
    }

    suspend fun saveLogin(context: Context, loginData: LoginEntity, loggedIn: Boolean = true) {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN] = loggedIn
            prefs[LOGIN] = loginData.login
            prefs[PASSWORD] = loginData.password
            prefs[SCHOOL_ID] = loginData.schoolId
        }
    }

    suspend fun saveEsiaLogin(context: Context, loginState: String, userId: String) {
        context.dataStore.edit { prefs ->
            prefs[LOGIN] = ""
            prefs[PASSWORD] = ""
            prefs[SCHOOL_ID] = -1
            prefs[ESIA_LOGIN_STATE] = loginState
            prefs[ESIA_USER_ID] = userId
        }
    }

    suspend fun logout(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN] = false
            prefs[LOGIN] = ""
            prefs[PASSWORD] = ""
            prefs[SCHOOL_ID] = 0
        }
    }
}