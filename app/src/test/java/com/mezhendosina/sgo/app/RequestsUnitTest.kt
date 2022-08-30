package com.mezhendosina.sgo.app

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.grades.GradesRepository
import com.mezhendosina.sgo.app.model.login.LoginEntity
import com.mezhendosina.sgo.app.model.login.LoginRepository
import com.mezhendosina.sgo.data.GradesFromHtml
import com.mezhendosina.sgo.data.requests.SourceProviderHolder
import com.mezhendosina.sgo.data.requests.login.entities.LogoutRequestEntity
import com.mezhendosina.sgo.data.toMD5
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class RequestsUnitTest {

    private val sourcesProvider = SourceProviderHolder.sourcesProvider

    private val loginSource = sourcesProvider.getLoginSource()
    private val gradesSource = sourcesProvider.getGradesSource()
    private val loginRepository = LoginRepository(loginSource)

    private val singleton = Singleton

    @Before
    fun login() {
        runBlocking {
            loginSource.loginData()
            val getData = loginSource.getData()

            val login = loginSource.login(
                LoginEntity(
                    2,
                    1,
                    -1,
                    1,
                    2,
                    89,
                    "МеньшенинЕ1",
                    "285639".toMD5(),
                    getData.lt,
                    getData.salt,
                    getData.ver
                )
            )
            singleton.at = login.at
            loginRepository.at = login.at
        }
    }

    @Test
    fun grades() {
        val gradesRepository = GradesRepository(gradesSource)

        runBlocking {
            val gradeOptions = gradesRepository.loadGradesOptions()
            val getGradesRequest = gradesSource.getGrades(
                singleton.at,
                gradeOptions.PCLID.value,
                gradeOptions.ReportType.first { it.is_selected }.value,
                gradeOptions.TERMID.first().value,
                gradeOptions.SID.value
            ).body()?.string() ?: ""

            val gradesList = GradesFromHtml().extractGrades(getGradesRequest)
            println(gradesList)

        }
    }

    @After
    fun logout() {
        runBlocking {
            loginRepository.logout()

        }
    }

}

