package com.che.zadali.sgoapp.data.schools


import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.google.gson.annotations.SerializedName

data class SchoolsList(
    @SerializedName("SchoolItems")
    val schoolItems: List<SchoolItem>
)