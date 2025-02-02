package com.tiozao.cdd.loja.controller.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.management.InvalidAttributeValueException
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException


@ControllerAdvice
internal class ErrorHandlingControllerAdvice {
    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun onConstraintValidationException(
        e: ConstraintViolationException
    ): ValidationErrorResponse {
        val error = ValidationErrorResponse()
        for (violation in e.getConstraintViolations()) {
            error.getViolations().add(
                getViolation(violation)

            )
        }
        return error
    }

    private fun getViolation(violation: ConstraintViolation<*>?): Violation {
        var lastViolation = violation!!.propertyPath.filter { !it.name.isNullOrBlank() }
            .last()
              return Violation(lastViolation.name, violation.message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun onMethodArgumentNotValidException(
        e: MethodArgumentNotValidException
    ): ValidationErrorResponse {
        val error = ValidationErrorResponse()
        for (fieldError in e.bindingResult.fieldErrors) {
            error.getViolations().add(
                Violation(fieldError.field, fieldError.defaultMessage)
            )
        }
        return error
    }


    @ExceptionHandler(InvalidAttributeValueException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun onMethodArgumentNotValidException(
        e: InvalidAttributeValueException
    ): ValidationErrorResponse {
        var error = ValidationErrorResponse()
        error.getViolations().add(
            Violation("modelo", e.message)
        )
        return error
    }

}

class ValidationErrorResponse {
    private var errorList : MutableList<Violation> = mutableListOf()
    fun getViolations(): MutableList<Violation>  {
        return this.errorList
    }
}

data class Violation(
    var fieldName: String,
    var message: String?
)
