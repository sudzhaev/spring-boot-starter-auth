package com.sudzhaev.auth.impl

import com.sudzhaev.auth.FailureHandler
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class DefaultFailureHandler<FAILURE>(
    private val oauthFailureStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    private val userNotResolvedStatus: HttpStatus = HttpStatus.UNAUTHORIZED,
    private val anyExceptionStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : FailureHandler<FAILURE> {

    private val log = LoggerFactory.getLogger(DefaultFailureHandler::class.java)

    override fun handleOauthFailure(failure: FAILURE, response: HttpServletResponse) {
        log.error("error occurred while authenticating user: $failure")
        response.status = oauthFailureStatus.value()
    }

    override fun handleUserNotResolved(request: HttpServletRequest, response: HttpServletResponse) {
        log.error("user not resolved: ${formatRequestForLogging(request)}")
        response.status = userNotResolvedStatus.value()
    }

    override fun handleAnyException(request: HttpServletRequest, response: HttpServletResponse, exception: Exception) {
        log.error("internal server error: ${formatRequestForLogging(request)}")
        response.status = anyExceptionStatus.value()
    }

    private fun formatRequestForLogging(request: HttpServletRequest): String {
        // TODO: log headers and cookies
        return "${request.method} ${request.requestURI}"
    }
}
