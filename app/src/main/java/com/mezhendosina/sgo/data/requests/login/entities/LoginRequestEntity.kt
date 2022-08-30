package com.mezhendosina.sgo.data.requests.login.entities

data class LoginRequestEntity(
    val LoginType: Int,
    val cid: Int,
    val sid: Int,
    val pid: Int,
    val cn: Int,
    val sft: Int,
    val scid: Int,
    val UN: String,
    val PW: String,
    val lt: String,
    val pw2: String,
    val ver: String
)