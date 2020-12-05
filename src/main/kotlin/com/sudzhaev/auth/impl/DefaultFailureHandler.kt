package com.sudzhaev.auth.impl

import com.sudzhaev.auth.FailureHandler
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import javax.servlet.http.HttpServletResponse

class DefaultFailureHandler<FAILURE>(private val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR)
    : FailureHandler<FAILURE> {

    private val log = LoggerFactory.getLogger(DefaultFailureHandler::class.java)

    override fun handle(failure: FAILURE, response: HttpServletResponse) {
        log.error("error occurred while authenticating user: $failure")
        response.status = status.value()
    }
}
