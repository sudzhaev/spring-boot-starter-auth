package com.sudzhaev.auth

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Service to retrieve user from request based on session and create session for user while authorizing
 * Session can be passed in request however you want (in headers, body etc.)
 *
 */
interface SessionService<USER> {

    /**
     * Retrieves session from request and resolve user by it.
     * @return user or null in request has no session
     */
    fun resolveUser(request: HttpServletRequest): USER?

    /**
     * Creates session for user and sets this session in response.
     */
    fun createSession(user: USER, response: HttpServletResponse)
}
