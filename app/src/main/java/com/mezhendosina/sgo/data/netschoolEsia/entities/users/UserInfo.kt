package com.mezhendosina.sgo.data.netschoolEsia.entities.users


import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("children")
    val children: Any,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isParent")
    val isParent: Boolean,
    @SerializedName("isStaff")
    val isStaff: Boolean,
    @SerializedName("isStudent")
    val isStudent: Boolean,
    @SerializedName("loginName")
    val loginName: String,
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("organizations")
    val organizationInfos: List<OrganizationInfo>
)