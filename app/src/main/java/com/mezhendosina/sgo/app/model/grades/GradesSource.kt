package com.mezhendosina.sgo.app.model.grades

import okhttp3.ResponseBody
import retrofit2.Response

interface GradesSource {

    suspend fun getParentInfoLetter(at: String): Response<ResponseBody>

    suspend fun getGrades(
        at: String,
        pclid: String,
        reportType: String,
        termID: String,
        sid: String
    ): Response<ResponseBody>

}