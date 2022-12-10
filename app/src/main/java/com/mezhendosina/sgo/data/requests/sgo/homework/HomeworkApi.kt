package com.mezhendosina.sgo.data.requests.sgo.homework

import com.mezhendosina.sgo.data.requests.sgo.diary.entities.AssignmentTypesResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface HomeworkApi {

    @GET("webapi/grade/assignment/types")
    suspend fun assignmentTypes(
        @Query("all") all: Boolean = true
    ): List<AssignmentTypesResponseEntity>


    @POST("webapi/student/diary/get-attachments")
    suspend fun getAttachments(
        @Query("studentId") studentId: Int,
        @Body attachmentsIds: AttachmentsRequestEntity
    ): List<AttachmentsResponseEntity>

    /**
    Получение прикрепленных ответов на задание
     */
    @GET("/webapi/assignments/{assignmentId}/answers")
    suspend fun getAnswer(
        @Path("assignmentId") assignmentId: Int,
        @Query("studentID") studentId: Int
    ): List<GetAnswerResponseEntity>

    @GET("webapi/student/diary/assigns/{assignID}")
    suspend fun getAboutAssign(
        @Path("assignID") assignId: Int,
        @Query("studentId") studentId: Int
    ): AssignResponseEntity

    //TODO move attachments requests into AttachmentsApi.kt
    @GET("webapi/attachments/{attachmentId}")
    @Streaming
    suspend fun downloadAttachment(
        @Path("attachmentId") attachmentId: Int
    ): Response<ResponseBody>

    @POST("/webapi/attachments/{attachmentId}/delete")
    suspend fun deleteAttachment(
        @Path("attachmentId") attachmentId: Int,
        @Body deleteAttachmentRequestEntity: DeleteAttachmentRequestEntity
    ): Response<ResponseBody>?

    @POST("/webapi/attachments/{attachmentId}/description")
    suspend fun editAttachmentDescription(
        @Path("attachmentId") attachmentId: Int,
        @Body description: String
    ): String

    @POST("webapi/assignments/{assignmentId}/answers")
    suspend fun sendTextAnswer(
        @Path("assignmentId") assignmentId: Int,
        @Query("studentId") studentId: Int,
        @Body answer: String
    ): Response<ResponseBody>

    @Multipart
    @POST("webapi/attachments")
    suspend fun sendFileAttachment(
        @Part file: MultipartBody.Part,
        @Part("data") data: SendFileRequestEntity
    ): Int
}