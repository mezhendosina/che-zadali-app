package com.mezhendosina.sgo.data.requests.login

import com.mezhendosina.sgo.app.model.login.LoginEntity
import com.mezhendosina.sgo.app.model.login.LoginSource
import com.mezhendosina.sgo.data.requests.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.login.entities.GetDataResponseEntity
import com.mezhendosina.sgo.data.requests.login.entities.LoginResponseEntity
import com.mezhendosina.sgo.data.requests.login.entities.LogoutRequestEntity
import com.mezhendosina.sgo.data.requests.login.entities.StudentResponseEntity
import com.mezhendosina.sgo.data.toMD5

class RetrofitLoginSource(
    config: RetrofitConfig
) : BaseRetrofitSource(config), LoginSource {

    private val loginApi = retrofit.create(LoginApi::class.java)

    override suspend fun loginData() = wrapRetrofitExceptions { loginApi.loginData() }

    override suspend fun getData(): GetDataResponseEntity =
        wrapRetrofitExceptions {
            loginApi.getData()
        }

    override suspend fun login(loginEntity: LoginEntity): LoginResponseEntity =
        wrapRetrofitExceptions {
            val passwordMD5 = (loginEntity.salt + loginEntity.password).toMD5()

            loginApi.login(
                loginType = 1,
                cid = loginEntity.countryId,
                sid = loginEntity.stateId,
                pid = loginEntity.provinceId,
                cn = loginEntity.cityId,
                sft = loginEntity.schoolType,
                scid = loginEntity.schoolId,
                UN = loginEntity.login,
                PW = passwordMD5.slice(0..5),
                pw2 = passwordMD5,
                lt = loginEntity.lt,
                ver = loginEntity.ver
            )
        }

    override suspend fun getStudents(): List<StudentResponseEntity>? =
        wrapRetrofitExceptions {
            loginApi.getStudents().body()
        }

    override suspend fun logout(logoutRequestEntity: LogoutRequestEntity) =
        wrapRetrofitExceptions {
            loginApi.logout(logoutRequestEntity.at)
        }
}