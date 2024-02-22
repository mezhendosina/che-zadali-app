package com.mezhendosina.sgo.data.netschoolEsia.entities.users


import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.data.netschoolEsia.entities.common.Clazz

data class OrganizationInfo(
    @SerializedName("classes")
    val clazzez: List<Clazz>,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("organization")
    val organization: Organization
)