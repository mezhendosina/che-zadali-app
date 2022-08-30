package com.mezhendosina.sgo.data.requests.grades

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface GradesApi {

    @Headers("Referer: https://sgo.edu-74.ru/angular/school/reports/")
    @POST("/asp/Reports/ReportParentInfoLetter.asp")
    @FormUrlEncoded
    suspend fun getParentInfoLetter(
        @Field("at") at: String,
        @Field("RPNAME") rpname: String = "Информационное письмо для родителей",
        @Field("RPTID") rptid: String = "ParentInfoLetter"
    ): Response<ResponseBody>

    @POST("/asp/Reports/ParentInfoLetter.asp")
    @FormUrlEncoded
    suspend fun getGrades(
        @Field("LoginType") loginType: String = "0",
        @Field("AT") at: String,
        @Field("PP") pp: String = "/asp/Reports/ReportParentInfoLetter.asp",
        @Field("BACK") back: String = "/asp/Reports/ReportParentInfoLetter.asp",
        @Field("ThmID") thmId: String = "",
        @Field("RPTID") rptid: String = "ParentInfoLetter",
        @Field("A") a: String = "",
        @Field("NA") na: String = "",
        @Field("TA") ta: String = "",
        @Field("RT") rt: String = "",
        @Field("RP") rp: String = "",
        @Field("dtWeek") dtWeek: String = "",
        @Field("PCLID") pclid: String,
        @Field("ReportType") reportType: String,
        @Field("TERMID") termId: String,
        @Field("SID") sid: String
    ): Response<ResponseBody>
}