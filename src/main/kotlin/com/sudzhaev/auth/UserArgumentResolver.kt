package com.sudzhaev.auth

import com.sudzhaev.auth.annotation.Unauthorized
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

    override fun resolveArgument(
        parameter: MethodParameter,
        modelAndViewContainer: ModelAndViewContainer?,
        request: NativeWebRequest,
        webDataBinderFactory: WebDataBinderFactory?
    ): Any? {
        val httpServletRequest = request.getNativeRequest(HttpServletRequest::class.java)
            ?: error("Cannot get HttpServletRequest")
        val attribute = httpServletRequest.getAttribute(AUTH_USER)
        if (attribute != null) {
            return attribute
        }
        if (parameter.isOptional && parameter.hasMethodAnnotation(Unauthorized::class.java)) {
            return null
        }
        error("Authorized user not found in HttpServletRequest")
    }
}
