package com.municipality.backend.application.complaint

import com.municipality.backend.domain.model.complaint.ComplaintCode
import com.municipality.backend.domain.service.complaint.Complaints
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.concurrent.locks.ReentrantLock

const val LENGTH = 8
const val UPPERCASES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
const val LOWERCASES = "abcdefghijklmnopqrstuvwxyz"
const val DIGITS = "0123456789"

@Component
class ComplaintCodeGenerator(
    private val complaints: Complaints
) {
    private val symbols = UPPERCASES + LOWERCASES + DIGITS

    fun generate(): ComplaintCode {
        val random = SecureRandom()
        val buffer = CharArray(LENGTH)
        for (i in buffer.indices) {
            val randomPosition = random.nextInt(symbols.length)
            buffer[i] = symbols[randomPosition]
        }
        val complaintCode = ComplaintCode(String(buffer))

        if (complaints.exists(complaintCode))
            return generate();

        return complaintCode
    }
}