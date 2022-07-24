package com.mezhendosina.sgo.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.data.layouts.password.Password
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.net.URI

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class SettingsLoginData(
    val cid: String,
    val sid: String,
    val pid: String,
    val cn: String,
    val sft: String,
    val scid: String,
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

        val CID = stringPreferencesKey("cid")
        val SID = stringPreferencesKey("sid")
        val pid = stringPreferencesKey("pid")
        val CN = stringPreferencesKey("cn")
        val SFT = stringPreferencesKey("sft")
        val SCID = stringPreferencesKey("scid")
    }

    val loggedIn = context.dataStore.data.map { it[LOGGED_IN] ?: false }
    val theme = context.dataStore.data.map {
        it[THEME] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    val currentUserId = context.dataStore.data.map { it[CURRENT_USER_ID] ?: 0 }
    val currentTrimId = context.dataStore.data.map { it[CURRENT_TRIM_ID] }
    val photoFileUri = context.dataStore.data.map { it[PHOTO_FILE_URI] }

    suspend fun saveALl(loginData: SettingsLoginData) {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN] = true
            prefs[LOGIN] = loginData.UN
            prefs[PASSWORD] = loginData.PW
            prefs[CID] = loginData.cid
            prefs[SID] = loginData.sid
            prefs[pid] = loginData.pid
            prefs[CN] = loginData.cn
            prefs[SFT] = loginData.sft
            prefs[SCID] = loginData.scid
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
            prefs[CID] = ""
            prefs[SID] = ""
            prefs[pid] = ""
            prefs[CN] = ""
            prefs[SFT] = ""
            prefs[SCID] = ""
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
                it[CID] ?: "0",
                it[SID] ?: "0",
                it[pid] ?: "0",
                it[CN] ?: "0",
                it[SFT] ?: "0",
                it[SCID] ?: "0",
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