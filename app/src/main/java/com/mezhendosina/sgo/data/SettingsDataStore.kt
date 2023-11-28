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
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
class SettingsDataStore @Inject constructor(@ApplicationContext private val context: Context) :
    AppSettings {

    companion object {
        val REGION_URL = stringPreferencesKey("region")
        val SCHOOL_ID = intPreferencesKey("scid")

        val LOGGED_IN = booleanPreferencesKey("logged_in")
        val LOGIN = stringPreferencesKey("login")

        val PASSWORD = stringPreferencesKey("password")
        val ESIA_LOGIN_STATE = stringPreferencesKey("esia_login_state")

        val ESIA_USER_ID = stringPreferencesKey("esia_user_id")
        val CURRENT_USER_ID = intPreferencesKey("current_user_id")

        val CURRENT_TRIM_ID = stringPreferencesKey("current_trim_id")
        val TRIM_ID = intPreferencesKey("trim_id")

        val THEME = intPreferencesKey("theme")
        val LAST_VERSION_NUMBER = intPreferencesKey("last_version_number")
        val SHOW_UPDATE_DIALOG = booleanPreferencesKey("show_update_dialog")
        val SORT_GRADES_BY = intPreferencesKey("sort_grades_by")

        val DIARY_STYLE = stringPreferencesKey("diary_style")

        val SKIP_SUNDAY = booleanPreferencesKey("skip_sunday")
    }

    override fun <T> getValue(value: Preferences.Key<T>): Flow<T?> {
        return context.dataStore.data.map { it[value] }
    }

    override suspend fun <T> setValue(value: Preferences.Key<T>, key: T) {
        context.dataStore.edit {
            it[value] = key
        }
    }

    override suspend fun saveLogin(loginData: LoginEntity, loggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN] = loggedIn
            prefs[LOGIN] = loginData.login
            prefs[PASSWORD] = loginData.password
            prefs[SCHOOL_ID] = loginData.schoolId
        }
    }

    override suspend fun saveEsiaLogin(loginState: String, userId: String) {
        context.dataStore.edit { prefs ->
            prefs[LOGIN] = ""
            prefs[PASSWORD] = ""
            prefs[SCHOOL_ID] = -1
            prefs[ESIA_LOGIN_STATE] = loginState
            prefs[ESIA_USER_ID] = userId
        }
    }

    override suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN] = false
            prefs[LOGIN] = ""
            prefs[PASSWORD] = ""
            prefs[SCHOOL_ID] = 0
        }
    }
}