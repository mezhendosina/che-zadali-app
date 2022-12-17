package com.mezhendosina.sgo.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.mezhendosina.sgo.app.model.login.LoginEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class SettingsLoginData(
    val cid: Int,
    val sid: Int,
    val pid: Int,
    val cn: Int,
    val sft: Int,
    val scid: Int,
    val UN: String,
    val PW: String,
)

class Settings(val context: Context) {


    companion object {
        val REGION_URL = stringPreferencesKey("region")

        val LOGGED_IN = booleanPreferencesKey("logged_in")

        val LOGIN = stringPreferencesKey("login")
        val PASSWORD = stringPreferencesKey("password")

        val CURRENT_USER_ID = intPreferencesKey("current_user_id")
        val THEME = intPreferencesKey("theme")
        val CURRENT_TRIM_ID = stringPreferencesKey("current_trim_id")
        val LAST_VERSION_NUMBER = intPreferencesKey("last_version_number")
        val SHOW_UPDATE_DIALOG = booleanPreferencesKey("show_update_dialog")
        val SORT_GRADES_BY = intPreferencesKey("sort_grades_by")

        val CID = intPreferencesKey("cid")
        val SID = intPreferencesKey("sid")
        val pid = intPreferencesKey("pid")
        val CN = intPreferencesKey("cn")
        val SFT = intPreferencesKey("sft")
        val SCID = intPreferencesKey("scid")
    }

    val loggedIn = context.dataStore.data.map { it[LOGGED_IN] ?: false }
    val theme = context.dataStore.data.map {
        it[THEME] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    val currentUserId = context.dataStore.data.map { it[CURRENT_USER_ID] ?: 0 }
    val currentTrimId = CURRENT_TRIM_ID.getValue()
    val regionUrl = REGION_URL.getValue()
    val lastVersionNumber = LAST_VERSION_NUMBER.getValue()
    val showUpdateDialog = SHOW_UPDATE_DIALOG.getValue()
    val sortGradesBy = SORT_GRADES_BY.getValue()

    private fun <T> Preferences.Key<T>.getValue(): Flow<T?> =
        context.dataStore.data.map { it[this] }

    suspend fun <T> setPreference(key: Preferences.Key<T>, value: T) = context.dataStore.edit {
        it[key] = value
    }

    suspend fun setRegion(regionUrl: String) {
        context.dataStore.edit {
            it[REGION_URL] = regionUrl
        }
    }

    suspend fun saveALl(loginData: LoginEntity, loggedId: Boolean = true) {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN] = loggedId
            prefs[LOGIN] = loginData.login
            prefs[PASSWORD] = loginData.password
            prefs[CID] = loginData.countryId
            prefs[SID] = loginData.stateId
            prefs[pid] = loginData.provinceId
            prefs[CN] = loginData.cityId
            prefs[SFT] = loginData.schoolType
            prefs[SCID] = loginData.schoolId
        }
    }

    suspend fun setCurrentUserId(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[CURRENT_USER_ID] = id
        }
    }

    suspend fun changeLoggedIn(loggedId: Boolean) =
        context.dataStore.edit { it[LOGGED_IN] = loggedId }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN] = false
            prefs[LOGIN] = ""
            prefs[PASSWORD] = ""
            prefs[CID] = 0
            prefs[SID] = 0
            prefs[pid] = 0
            prefs[CN] = 0
            prefs[SFT] = 0
            prefs[SCID] = 0
        }
    }

    suspend fun setTheme(selectedTheme: Int) {
        context.dataStore.edit { prefs ->
            prefs[THEME] = selectedTheme
        }
    }

    suspend fun getLoginData(): SettingsLoginData {
        context.dataStore.data.first().let {
            return SettingsLoginData(
                it[CID] ?: 0,
                it[SID] ?: 0,
                it[pid] ?: 0,
                it[CN] ?: 0,
                it[SFT] ?: 0,
                it[SCID] ?: 0,
                it[LOGIN] ?: "",
                it[PASSWORD] ?: ""
            )
        }
    }

    suspend fun changePassword(newPassword: String) = context.dataStore.edit { prefs ->
        prefs[PASSWORD] = newPassword
    }

    suspend fun changeTRIMId(newId: String) = context.dataStore.edit { prefs ->
        prefs[CURRENT_TRIM_ID] = newId
    }

    suspend fun changeLastVersionNumber(versionNumber: Int) = context.dataStore.edit {
        it[LAST_VERSION_NUMBER] = versionNumber
    }

    suspend fun changeShowUpdateDialog(b: Boolean) = context.dataStore.edit {
        it[SHOW_UPDATE_DIALOG] = b
    }
}