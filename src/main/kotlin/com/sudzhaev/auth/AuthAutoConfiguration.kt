package com.sudzhaev.auth

import com.sudzhaev.auth.impl.DefaultFailureHandler
import com.sudzhaev.auth.impl.DefaultSessionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AuthAutoConfiguration : WebMvcConfigurer {

    @Bean
    fun <USER, FAILURE> authFilter(
            @Value("#{systemProperties['com.sudzhaev.auth.auth-filter-order'] ?: T(java.lang.Integer).MIN_VALUE}") order: Int,
            sessionService: SessionService<USER>,
            failureHandler: FailureHandler<FAILURE>,
            oauthAdapters: List<OauthAdapter<USER, FAILURE>>
    ): AuthFilter<USER, FAILURE> {
        return AuthFilter(order, sessionService, failureHandler, oauthAdapters)
    }

    @Bean
    @ConditionalOnMissingBean(SessionService::class)
    fun defaultSessionService(): SessionService<*> {
        return DefaultSessionService<Any>()
    }

    @Bean
    @ConditionalOnMissingBean(FailureHandler::class)
    fun defaultFailureHandler(): FailureHandler<*> {
        return DefaultFailureHandler<Any>()
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(UserArgumentResolver())
    }
}
