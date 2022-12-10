package com.mezhendosina.sgo.data.requests.sgo.grades

import com.mezhendosina.sgo.app.model.grades.GradesSource
import com.mezhendosina.sgo.data.requests.sgo.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.sgo.base.RetrofitConfig
import okhttp3.ResponseBody
import retrofit2.Response

class RetrofitGradesService(config: RetrofitConfig) : BaseRetrofitSource(config), GradesSource {

    private val gradesApi = retrofit.create(GradesApi::class.java)

    override suspend fun getParentInfoLetter(at: String): Response<ResponseBody> =
        wrapRetrofitExceptions {
            gradesApi.getParentInfoLetter(at)
        }

    override suspend fun getGrades(
        at: String,
        pclid: String,
        reportType: String,
        termID: String,
        sid: String
    ): Response<ResponseBody> = wrapRetrofitExceptions {
        gradesApi.getGrades(
            at = at,
            pclid = pclid,
            reportType = reportType,
            termId = termID,
            sid = sid
        )
    }

}