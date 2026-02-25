package org.pigletsinc.syncplay.web.configuration

import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val message = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity(mapOf("error" to message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<Map<String, String>> {
        val errorResponse = mapOf("error" to (ex.reason?.ifBlank { null } ?: ex.message.ifBlank { null } ?: "Request failed"))
        return ResponseEntity(errorResponse, ex.statusCode)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<Map<String, String>> {
        val errorMessage = extractErrorMessage(ex)
        val errorResponse = mapOf("error" to errorMessage)
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFound(): ResponseEntity<Void> = ResponseEntity.notFound().build()

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<Map<String, String>> {
        log.error("Unexpected error", ex)
        val errorResponse = mapOf("error" to (ex.message ?: "An unexpected error occurred."))
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
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
