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

package com.mezhendosina.sgo.data.requests.sgo.school.entities

import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.app.model.chooseSchool.SchoolUiEntity

data class SchoolResponseEntity(
    @SerializedName("address")
    val address: Any,
    @SerializedName("cityId")
    val cityId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("inn")
    val inn: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("ogrn")
    val ogrn: String,
    @SerializedName("provinceId")
    val provinceId: Int,
    @SerializedName("shortName")
    val shortName: String
) {
    fun toUiEntity(): SchoolUiEntity = SchoolUiEntity(
        name.replace(shortName, "").replace("(", "").replace(")", ""),
        cityId,
        provinceId,
        shortName,
        id
    )
}