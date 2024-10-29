package org.pigletsinc.syncplay.exception

import org.postgresql.util.PSQLException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<Map<String, String>> {
        val errorResponse = mapOf("error" to (ex.reason ?: "Unknown error"))
        return ResponseEntity(errorResponse, ex.statusCode)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<Map<String, String>> {
        val errorResponse = mapOf("error" to "An unexpected error occurred.")
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<Map<String, String>> {
        val errorMessage = extractErrorMessage(ex)
        val errorResponse = mapOf("error" to errorMessage)
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    private fun extractErrorMessage(ex: DataIntegrityViolationException): String {
        val cause = ex.rootCause as? PSQLException
        return when {
            cause?.message?.contains("duplicate key value violates unique constraint") == true -> {
                val emailPattern = Regex("Key \\(email\\)=\\(([^)]+)\\)")
                val matchResult = emailPattern.find(cause.message!!)
                matchResult?.groups?.get(1)?.value?.let { email ->
                    "User with mail $email already exists"
                } ?: "Duplicate entry exists"
            }
            else -> "Data integrity violation occurred"
        }
    }
}
