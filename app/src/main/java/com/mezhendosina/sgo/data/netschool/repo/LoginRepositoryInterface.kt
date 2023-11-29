package com.mezhendosina.sgo.data.netschool.repo

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.uiEntities.SchoolUiEntity
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.api.login.LoginEntity
import com.mezhendosina.sgo.data.netschool.api.login.entities.StudentResponseEntity
import com.mezhendosina.sgo.data.netschool.api.login.entities.accountInfo.User
import com.mezhendosina.sgo.data.requests.sgo.login.entities.LoginResponseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

interface LoginRepositoryInterface {
    suspend fun findSchool(schoolId: Int): SchoolUiEntity

    fun getSchools(): StateFlow<List<SchoolUiEntity>>

    suspend fun mapSchools(name: String)

    suspend fun login(
        login: String? = null,
        password: String? = null,
        schoolId: Int? = null,
        firstLogin: Boolean = true,
        onOneUser: () -> Unit = {},
        onMoreUser: (List<StudentResponseEntity>) -> Unit = {}
    )

    suspend fun getGosuslugiUsers(loginState: String): List<User>
    suspend fun gosuslugiLogin(
        firstLogin: Boolean = false
    )

    suspend fun logout()
}