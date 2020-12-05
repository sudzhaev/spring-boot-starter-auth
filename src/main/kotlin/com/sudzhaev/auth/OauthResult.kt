package com.sudzhaev.auth

sealed class OauthResult<SUCCESS_TYPE, FAILURE_TYPE> {
    data class Success<SUCCESS_TYPE>(val data: SUCCESS_TYPE) : OauthResult<SUCCESS_TYPE, Any>()
    data class Failure<FAILURE_TYPE>(val data: FAILURE_TYPE) : OauthResult<Any, FAILURE_TYPE>()
}
