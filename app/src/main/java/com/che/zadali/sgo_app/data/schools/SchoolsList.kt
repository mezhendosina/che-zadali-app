package com.che.zadali.sgo_app.data.schools


import com.google.gson.annotations.SerializedName

data class SchoolsList(
    @SerializedName("SchoolItems")
    val schoolItems: List<SchoolItem>
)