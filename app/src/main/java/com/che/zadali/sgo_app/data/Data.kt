package com.che.zadali.sgo_app.data

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.che.zadali.sgo_app.data.diary.Diary
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

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
    @SerializedName("output")
    val output: String
)

//App settings
object Settings {
    val LOGGED_IN: Preferences.Key<Boolean> = booleanPreferencesKey("LOGGED_IN")

    val LOGIN: Preferences.Key<String> = stringPreferencesKey("LOGIN")
    val PASSWORD: Preferences.Key<String> = stringPreferencesKey("PASSWORD")
    val SCHOOL_ID: Preferences.Key<Int> = intPreferencesKey("SCHOOL_ID")
    val CITY_ID: Preferences.Key<Int> = intPreferencesKey("CITY_ID")
    val PROVINCE_ID: Preferences.Key<Int> = intPreferencesKey("PROVINCE_ID")

    val HOLIDAY_VIEW: Preferences.Key<Boolean> = booleanPreferencesKey("HOLIDAY_VIEW")
}


