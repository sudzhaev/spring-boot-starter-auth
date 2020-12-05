package com.sudzhaev.auth

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
    fun handle(failure: FAILURE, response: HttpServletResponse)
}
