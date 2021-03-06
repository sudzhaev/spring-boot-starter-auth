package com.sudzhaev.auth

import com.sudzhaev.auth.annotation.Unauthorized
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.core.Ordered
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import javax.servlet.FilterChain
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthFilter<USER, FAILURE>(
        private val order: Int,
        private val sessionService: SessionService<USER>,
        private val failureHandler: FailureHandler<FAILURE>,
        oauthAdapters: List<OauthAdapter<USER, FAILURE>>,
) : HttpFilter(), Ordered, ApplicationListener<ContextRefreshedEvent> {

    private val log = LoggerFactory.getLogger(AuthFilter::class.java)
    private val redirectUriToOauthAdapterMap = oauthAdapters.map { it.redirectUri to it }.toMap()
    private lateinit var requestMappingHandlerMapping: RequestMappingHandlerMapping

    override fun doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            doFilterImpl(request, response, chain)
        } catch (e: Exception) {
            log.error("Internal server error", e)
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        }
    }

    override fun getOrder(): Int = order

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        this.requestMappingHandlerMapping = event.applicationContext.getBean(RequestMappingHandlerMapping::class.java)
    }

    private fun doFilterImpl(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        if (isOauthRedirect(request)) {
            when (val oauthResult = redirectUriToOauthAdapterMap.getValue(request.requestURI).authenticateUser(request)) {
                is OauthResult.Success -> sessionService.createSession(oauthResult.data, response)
                is OauthResult.Failure -> failureHandler.handle(oauthResult.data, response)
            }
            return
        }

        if (unauthorizedAllowed(request) || tryAuthorize(request)) {
            chain.doFilter(request, response)
            return
        }

        response.status = HttpStatus.UNAUTHORIZED.value()
    }

    private fun isOauthRedirect(request: HttpServletRequest): Boolean {
        return request.method == HttpMethod.GET.name && request.requestURI in redirectUriToOauthAdapterMap
    }

    private fun unauthorizedAllowed(request: HttpServletRequest): Boolean {
        return (requestMappingHandlerMapping.getHandler(request)?.handler as? HandlerMethod)
                ?.hasMethodAnnotation(Unauthorized::class.java)
                ?: true
    }

    private fun tryAuthorize(request: HttpServletRequest): Boolean {
        val user = sessionService.resolveUser(request) ?: return false
        request.setAttribute(AUTH_USER, user)
        return true
    }
}
