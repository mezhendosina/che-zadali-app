package com.mezhendosina.sgo.data.layouts.login


import com.google.gson.annotations.SerializedName

data class AccountInfo(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("activeToken")
    val activeToken: Any,
    @SerializedName("canLogin")
    val canLogin: Boolean,
    @SerializedName("currentOrganization")
    val currentOrganization: CurrentOrganization,
    @SerializedName("loginTime")
    val loginTime: String,
    @SerializedName("organizations")
    val organizations: List<Organization>,
    @SerializedName("secureActiveToken")
    val secureActiveToken: String,
    @SerializedName("storeTokens")
    val storeTokens: Boolean,
    @SerializedName("user")
    val user: User,
    @SerializedName("userRoles")
    val userRoles: List<Any>
)