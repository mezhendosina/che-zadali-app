package com.mezhendosina.sgo.data.netschoolEsia.entities.education


import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.data.netschoolEsia.entities.common.School

data class EducationInfo(
    @SerializedName("class")
    val educationClassX: EducationClass,
    @SerializedName("isAddSchool")
    val isAddSchool: Boolean,
    @SerializedName("school")
    val school: School,
    @SerializedName("schoolyear")
    val schoolyear: SchoolYear
)