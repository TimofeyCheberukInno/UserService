package com.app.impl.advice;

import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.app.impl.exception.CardNotFoundException;
import com.app.impl.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status, String msg,
            String description
    ) {
		ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                msg,
                description
        );

		return new ResponseEntity<>(errorResponse, status);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex,
            WebRequest request
    ) {
		return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false)
        );
	}

	@ExceptionHandler(CardNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCardNotFoundException(
            CardNotFoundException ex,
            WebRequest request
    ) {
		return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false)
        );
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException ex,
            WebRequest request
    ) {
		return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false)
        );
	}

	@ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleHibernateConstraintViolationException(
			org.hibernate.exception.ConstraintViolationException ex,
            WebRequest request
    ) {
		return buildErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getDescription(false)
        );
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
			WebRequest request
    ) {
		return buildErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getDescription(false)
        );
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex,
            WebRequest request
    ) {
		return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false)
        );
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
			WebRequest request
    ) {
		String errors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

		return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                errors,
                request.getDescription(false)
        );
	}

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false)
        );
    }

	@ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleJakartaValidationConstraintViolationException(
			jakarta.validation.ConstraintViolationException ex,
            WebRequest request
    ) {
		String errors = ex.getConstraintViolations().stream()
				.map(error -> error.getPropertyPath() + ": " + error.getMessage())
                .collect(Collectors.joining(", "));

		return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                errors,
                request.getDescription(false)
        );
	}

	@ExceptionHandler(ClassCastException.class)
	public ResponseEntity<ErrorResponse> handleClassCastException(
            ClassCastException ex,
            WebRequest request
    ) {
		return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false)
        );
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            WebRequest request
    ) {
		return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false)
        );
	}
}