package cst.baseappsetup.data.repository

import cst.baseappsetup.data.api.AuthorizationAPI
import cst.baseappsetup.data.client.RefreshAuthAPIClient

object AuthorizationAPIRepository {

    private val authAPI: AuthorizationAPI by lazy {
        RefreshAuthAPIClient.retrofitClient.create(AuthorizationAPI::class.java)
    }

    fun refreshToken(refreshToken: String) =
        authAPI.refreshToken(
            refreshToken = refreshToken,
            grantType = AuthorizationAPI.GRANT_TYPE_REFRESH_TOKEN,
            scope = AuthorizationAPI.SCOPE_MOBILE
        )

}