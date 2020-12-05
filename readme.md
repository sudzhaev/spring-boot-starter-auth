# [Work in progress] Spring boot starter auth

Lightweight oauth implementation for spring boot.
Based on [scribe-java](https://github.com/scribejava/scribejava) library

## Usage
1. Implement and create a bean of `com.sudzhaev.auth.SessionService`.
2. Implement and create a bean of `com.sudzhaev.auth.OauthAdapter`. It can have multiple implementations (and therefore multiple beans) for different oauth providers. You can use `com.sudzhaev.auth.impl.DefaultOauthAdapter` for simplicity.
    1. By default if `OauthAdapter` returns `Failure` client will receive "internal server error". If you would like to override such behaviour create bean of type `com.sudzhaev.auth.FailureHandler`
3. `SessionService` and `OauthAdapters` must have **same generic type** for user.
4. To retrieve user from request in controller use `com.sudzhaev.auth.annotation.User` annotation
```
@PostMapping("/foo")
fun foo(@User user: YourUserDto) {
    ...
}
```
5. If you want to allow handler invocation without authorization use `com.sudzhaev.auth.annotation.Unauthorized` annotation
```
@Unauthorized
@PostMapping("/bar")
fun bar() {
    ...
}
```
6. ...
7. Profit
