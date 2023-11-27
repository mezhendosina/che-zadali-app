package com.mezhendosina.sgo.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.mezhendosina.sgo.data.netschool.api.login.LoginEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AppSettings {


    fun <T> getValue(value: Preferences.Key<T>): Flow<T?>

    suspend fun <T> setValue(value: Preferences.Key<T>, key: T)


    suspend fun saveLogin(loginData: LoginEntity, loggedIn: Boolean = true)

    suspend fun saveEsiaLogin(loginState: String, userId: String)

    suspend fun logout()
}