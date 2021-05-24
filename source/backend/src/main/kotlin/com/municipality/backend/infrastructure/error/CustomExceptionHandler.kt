package com.municipality.backend.infrastructure.error

import com.municipality.backend.application.user.*
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(RuntimeException::class)
    fun handleAll(exception: Exception, request: WebRequest?): ResponseEntity<Any> {
        var status: HttpStatus
        var error: ErrorCode

        when(exception) {
            is InsufficientPermissionException -> {
                status = HttpStatus.UNAUTHORIZED
                error = ErrorCode.UNAUTHORIZED
            }

            is MissingInformationException -> {
                status = HttpStatus.BAD_REQUEST
                error = ErrorCode.MISSING_INFORMATION
            }

            is WeakPasswordException -> {
                status = HttpStatus.BAD_REQUEST
                error = ErrorCode.WEAK_PASSWORD
            }

            is UsernameAlreadyExistsException -> {
                status = HttpStatus.BAD_REQUEST
                error = ErrorCode.USERNAME_EXISTS
            }

            is EmailAlreadyExistsException -> {
                status = HttpStatus.BAD_REQUEST
                error = ErrorCode.EMAIL_EXISTS
            }

            is InvalidEmailException -> {
                status = HttpStatus.BAD_REQUEST
                error = ErrorCode.INVALID_EMAIL
            }

            is IncorrectUsernameOrPasswordException -> {
                status = HttpStatus.BAD_REQUEST
                error = ErrorCode.INCORRECT_LOGIN_INFORMATION
            }

            else -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR
                error = ErrorCode.INTERNAL_ERROR
            }
        }

        return ResponseEntity(ErrorResponse(error), HttpHeaders(), status)
    }

}

data class ErrorResponse(val code: ErrorCode)