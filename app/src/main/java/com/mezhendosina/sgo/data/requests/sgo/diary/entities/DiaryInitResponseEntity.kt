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

package com.mezhendosina.sgo.data.requests.sgo.diary.entities


import com.google.gson.annotations.SerializedName

data class DiaryInitResponseEntity(
    @SerializedName("currentStudentId")
    val currentStudentId: Int,
    @SerializedName("externalUrl")
    val externalUrl: String,
    @SerializedName("maxMark")
    val maxMark: Int,
    @SerializedName("newDiskToken")
    val newDiskToken: String,
    @SerializedName("newDiskWasRequest")
    val newDiskWasRequest: Boolean,
    @SerializedName("students")
    val students: List<Student>,
    @SerializedName("ttsuRl")
    val ttsuRl: String,
    @SerializedName("weekStart")
    val weekStart: String,
    @SerializedName("weight")
    val weight: Boolean,
    @SerializedName("withLaAssigns")
    val withLaAssigns: Boolean,
    @SerializedName("yaClass")
    val yaClass: Boolean,
    @SerializedName("yaClassAuthUrl")
    val yaClassAuthUrl: Any
)

data class Student(
    @SerializedName("classId")
    val classId: Int,
    @SerializedName("className")
    val className: Any,
    @SerializedName("iupGrade")
    val iupGrade: Int,
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("studentId")
    val studentId: Int
)