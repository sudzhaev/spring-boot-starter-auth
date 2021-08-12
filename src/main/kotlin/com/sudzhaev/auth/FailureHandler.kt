package com.sudzhaev.auth

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Handler for failures happened during user authentication
 */
interface FailureHandler<FAILURE> {

    /**
     * @param failure – data from return value of OauthAdapter.authenticateUser
     * @param response – response where you can set appropriate status, body etc.
     * @see com.sudzhaev.auth.OauthAdapter.authenticateUser
     */
    fun handleOauthFailure(failure: FAILURE, response: HttpServletResponse)

    fun handleUserNotResolved(request: HttpServletRequest, response: HttpServletResponse)

    fun handleAnyException(request: HttpServletRequest, response: HttpServletResponse, exception: Exception)
}
