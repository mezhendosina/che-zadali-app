package com.mezhendosina.sgo.data.requests.other.entities.schools


import com.google.gson.annotations.SerializedName

data class SchoolsResponse(
    @SerializedName("SchoolItems")
    val schoolItems: List<SchoolItem>
)