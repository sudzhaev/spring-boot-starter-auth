package com.sudzhaev.auth

import com.sudzhaev.auth.annotation.User
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

class UserArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(User::class.java)
    }

    override fun resolveArgument(parameter: MethodParameter,
                                 modelAndViewContainer: ModelAndViewContainer?,
                                 request: NativeWebRequest,
                                 webDataBinderFactory: WebDataBinderFactory?): Any {
        val httpServletRequest = request.getNativeRequest(HttpServletRequest::class.java)
                ?: error("Cannot get HttpServletRequest")
        return httpServletRequest.getAttribute(AUTH_USER)
                ?: error("Authorized user not found in HttpServletRequest")
    }
}
