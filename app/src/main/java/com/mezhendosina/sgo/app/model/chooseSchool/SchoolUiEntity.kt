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

package com.mezhendosina.sgo.app.model.chooseSchool


import com.google.gson.annotations.SerializedName

data class SchoolUiEntity(
    @SerializedName("city")
    val city: String,
    @SerializedName("city_id")
    val cityId: Int,
    @SerializedName("province_id")
    val provinceId: Int,
    @SerializedName("school")
    val school: String,
    @SerializedName("SchoolId")
    val schoolId: Int
)