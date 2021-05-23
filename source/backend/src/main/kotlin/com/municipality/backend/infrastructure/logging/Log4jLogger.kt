package com.municipality.backend.infrastructure.logging

import com.municipality.backend.domain.model.core.error.ErrorCode
import com.municipality.backend.domain.service.core.Logger
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component

@Component
class Log4jLogger : Logger {

    override fun info(classToLogFor: Class<*>, message: String) {
        logger(classToLogFor).info(message)
    }

    override fun warn(classToLogFor: Class<*>, message: String) {
        logger(classToLogFor).warn(message)
    }

    override fun error(classToLogFor: Class<*>, errorCode: ErrorCode) {
        logger(classToLogFor).error(errorCode.name)
    }

    override fun error(classToLogFor: Class<*>, errorCode: ErrorCode, throwable: Throwable) {
        logger(classToLogFor).error(errorCode.name, throwable)
    }

    private fun logger(classToLogFor: Class<*>) = LogManager.getLogger(classToLogFor)
}