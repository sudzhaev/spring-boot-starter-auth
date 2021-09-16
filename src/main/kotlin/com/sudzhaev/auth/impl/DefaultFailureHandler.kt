package com.sudzhaev.auth.impl

import com.sudzhaev.auth.FailureHandler
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.filter.CommonsRequestLoggingFilter
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
        log.error("user not resolved: ${ToStringRequestHelper.build(request)}")
        response.status = userNotResolvedStatus.value()
    }

    override fun handleAnyException(request: HttpServletRequest, response: HttpServletResponse, exception: Exception) {
        log.error("internal server error: ${ToStringRequestHelper.build(request)}")
        response.status = anyExceptionStatus.value()
    }

    private object ToStringRequestHelper : CommonsRequestLoggingFilter() {

        init {
            isIncludeQueryString = true
            isIncludeClientInfo = true
            isIncludeHeaders = true
            isIncludePayload = true
        }

        fun build(request: HttpServletRequest): String {
            return createMessage(request, "{", "}")
        }
    }
}
