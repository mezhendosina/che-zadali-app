package com.che.zadali.sgoapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//App settings

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

