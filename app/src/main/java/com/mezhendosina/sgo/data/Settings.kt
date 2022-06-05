package com.mezhendosina.sgo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.mezhendosina.sgo.app.R
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

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


        val CID = stringPreferencesKey("cid")
        val SID = stringPreferencesKey("sid")
        val pid = stringPreferencesKey("pid")
        val CN = stringPreferencesKey("cn")
        val SFT = stringPreferencesKey("sft")
        val SCID = stringPreferencesKey("scid")
    }


    val currentUserId = context.dataStore.data.map {
        it[CURRENT_USER_ID] ?: 0
    }
    val loggedIn = context.dataStore.data.map {
        it[LOGGED_IN] ?: false
    }
    val theme = context.dataStore.data.map {
//        it[THEME] ?: R.id.same_as_system
    }


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
}