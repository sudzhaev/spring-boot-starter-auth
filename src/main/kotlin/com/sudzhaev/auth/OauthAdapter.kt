package com.sudzhaev.auth

import javax.servlet.http.HttpServletRequest

/**
 * Adapter used to interact with oauth provider.
 */
interface OauthAdapter<USER> {

    /**
     * redirect uri used by oauth provider.
     * Must be relative, without hostname
     */
    val redirectUri: String

    /**
     * Method invoked on redirect by oauth provider.
     */
    fun authenticateUser(request: HttpServletRequest): USER
}
