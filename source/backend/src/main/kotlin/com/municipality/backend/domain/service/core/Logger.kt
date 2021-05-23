package com.municipality.backend.domain.service.core

import com.municipality.backend.domain.model.core.error.ErrorCode

interface Logger {
    fun info(classToLogFor: Class<*>, message: String)
    fun warn(classToLogFor: Class<*>, message: String)
    fun error(classToLogFor: Class<*>, errorCode: ErrorCode)
    fun error(classToLogFor: Class<*>, errorCode: ErrorCode, throwable: Throwable)
}