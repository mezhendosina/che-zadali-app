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

package com.mezhendosina.sgo.data.requests.sgo.announcements


import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.Attachment

data class AnnouncementsResponseEntity(
    @SerializedName("attachments")
    val attachments: List<Attachment>,
    @SerializedName("author")
    val author: Author,
    @SerializedName("deleteDate")
    val deleteDate: Any,
    @SerializedName("description")
    val description: String,
    @SerializedName("em")
    val em: Em,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("postDate")
    val postDate: String,
    @SerializedName("recipientInfo")
    val recipientInfo: Any
)


data class Author(
    @SerializedName("fio")
    val fio: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nickName")
    val nickName: String
)

data class Em(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)