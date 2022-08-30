package com.mezhendosina.sgo.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.mezhendosina.sgo.app.model.login.LoginEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.net.URI

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
        val LOGGED_IN = booleanPreferencesKey("logged_in")

        val LOGIN = stringPreferencesKey("login")
        val PASSWORD = stringPreferencesKey("password")

        val CURRENT_USER_ID = intPreferencesKey("current_user_id")
        val THEME = intPreferencesKey("theme")
        val CURRENT_TRIM_ID = stringPreferencesKey("current_trim_id")
        val PHOTO_FILE_URI = stringPreferencesKey("photo_file_uri")

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
    val currentTrimId = context.dataStore.data.map { it[CURRENT_TRIM_ID] }
    val photoFileUri = context.dataStore.data.map { it[PHOTO_FILE_URI] }

    suspend fun saveALl(loginData: LoginEntity) {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN] = true
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

    suspend fun changePhotoURI(newURI: URI) = context.dataStore.edit {
        it[PHOTO_FILE_URI] = newURI.toString()
    }
}