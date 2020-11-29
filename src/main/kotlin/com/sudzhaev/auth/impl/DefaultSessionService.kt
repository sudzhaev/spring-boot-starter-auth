package com.sudzhaev.auth.impl

import com.sudzhaev.auth.SessionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Demo in-memory implementation of SessionService
 * NOT FOR PRODUCTION!!!
 */
class DefaultSessionService<USER> : SessionService<USER>, InitializingBean {

    private val log = LoggerFactory.getLogger(DefaultSessionService::class.java)
    private val sessionStorage = mutableMapOf<String, USER>()

    override fun afterPropertiesSet() {
        log.warn("Using DefaultSessionService. This implementation is not suitable for production!")
    }

    override fun resolveUser(request: HttpServletRequest): USER? {
        val session = request.getHeader(SESSION_HEADER) ?: return null
        return sessionStorage[session]
    }

    override fun createSession(user: USER, response: HttpServletResponse) {
        val session = user.hashCode().toString()
        sessionStorage[session] = user
        response.addCookie(Cookie(SESSION_HEADER, session))
    }

    companion object {
        private const val SESSION_HEADER = "X-SESSION"
    }
}
