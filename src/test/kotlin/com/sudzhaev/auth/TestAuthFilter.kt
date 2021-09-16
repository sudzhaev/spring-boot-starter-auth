package com.sudzhaev.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@SpringBootTest(classes = [AuthAutoConfiguration::class])
@EnableWebMvc
class TestAuthFilter {

    @Autowired
    private lateinit var authFilter: AuthFilter<User, *>

    @MockBean
    private lateinit var sessionService: SessionService<User>

    @Mock
    private lateinit var filterChain: FilterChain

    @Mock
    private lateinit var request: HttpServletRequest

    @Mock
    private lateinit var response: HttpServletResponse

    @Test
    fun test() {
        val user = User(1)
        sessionService.stub { on { resolveUser(request) } doReturn user }

        authFilter.doFilter(request, response, filterChain)

        verify(sessionService).resolveUser(request)

        val userCaptor = argumentCaptor<User>()
        verify(request).setAttribute(eq("com.sudzhaev.auth.AUTH_USER"), userCaptor.capture())
        assertThat(userCaptor.firstValue).isEqualTo(user)

        verify(filterChain).doFilter(request, response)
    }

    data class User(var id: Int)
}
