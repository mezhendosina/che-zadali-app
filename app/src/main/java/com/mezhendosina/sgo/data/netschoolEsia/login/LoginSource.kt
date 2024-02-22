package com.mezhendosina.sgo.data.netschoolEsia.login

import com.mezhendosina.sgo.data.netschoolEsia.entities.login.GetTokenResponse
import com.mezhendosina.sgo.data.netschoolEsia.entities.login.GetUsersResponseItem

interface LoginSource {
    /**
     * Получение токена с помощью deviceCode
     */
    suspend fun getToken(deviceCode: Int): GetTokenResponse

    /**
     *  Получение токена с помощью RefreshToken
     */
    suspend fun getToken(refreshToken: String): GetTokenResponse

    suspend fun getUsers(): List<GetUsersResponseItem>
}
