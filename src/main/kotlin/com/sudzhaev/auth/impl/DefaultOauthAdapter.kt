package com.sudzhaev.auth.impl

import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Response
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth20Service
import com.sudzhaev.auth.OauthAdapter
import java.net.URL
import javax.servlet.http.HttpServletRequest

abstract class DefaultOauthAdapter<USER>(
        final override val redirectUri: String,
        private val userInfoUrl: String,
        private val oauthService: OAuth20Service,
) : OauthAdapter<USER> {

    init {
        // TODO: add fail-fast checks for oauthService fields
        check(URL(oauthService.callback).path == redirectUri) {
            "oauthService.callback is different from redirectUri"
        }
    }

    override fun authenticateUser(request: HttpServletRequest): USER {
        val code = takeCodeFromRequest(request)
        val accessToken = oauthService.getAccessToken(code)
        val oAuthRequest = OAuthRequest(Verb.GET, userInfoUrl)
        oauthService.signRequest(accessToken, oAuthRequest)
        oauthService.execute(oAuthRequest).use {
            check(it.code == 200) { "oAuthRequest returned ${it.code}" }
            return handleOauthResponse(it)
        }
    }

    protected open fun takeCodeFromRequest(request: HttpServletRequest): String {
        return request.getParameter("code") ?: error("'code' not found in request")
    }

    abstract fun handleOauthResponse(response: Response): USER
}
