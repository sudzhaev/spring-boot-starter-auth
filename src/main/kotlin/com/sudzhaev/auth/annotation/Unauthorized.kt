package com.sudzhaev.auth.annotation

@Target(allowedTargets = [AnnotationTarget.FUNCTION])
@Retention(AnnotationRetention.RUNTIME)
annotation class Unauthorized
