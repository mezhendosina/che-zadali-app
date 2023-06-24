/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.data.netschool.api.attachments

import com.mezhendosina.sgo.data.netschool.api.attachments.entities.AttachmentsRequestEntity
import com.mezhendosina.sgo.data.netschool.api.attachments.entities.AttachmentsResponseEntity
import com.mezhendosina.sgo.data.netschool.api.attachments.entities.DeleteAttachmentRequestEntity
import com.mezhendosina.sgo.data.netschool.api.attachments.entities.SendFileRequestEntity
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface AttachmentsApi {
    @POST("webapi/student/diary/get-attachments")
    suspend fun getAttachments(
        @Query("studentId") studentId: Int,
        @Body attachmentsIds: AttachmentsRequestEntity
    ): List<AttachmentsResponseEntity>

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

    @GET("webapi/attachments/{attachmentId}")
    @Streaming
    suspend fun downloadAttachment(
        @Path("attachmentId") attachmentId: Int
    ): Response<ResponseBody>

    @POST("webapi/attachments/{attachmentId}/delete")
    suspend fun deleteAttachment(
        @Path("attachmentId") attachmentId: Int,
        @Body deleteAttachmentRequestEntity: DeleteAttachmentRequestEntity
    ): Response<ResponseBody>?

    @POST("webapi/attachments/{attachmentId}/description")
    suspend fun editAttachmentDescription(
        @Path("attachmentId") attachmentId: Int,
        @Body description: String
    ): String

}