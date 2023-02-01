package com.backend.arthere.global.error;

import com.backend.arthere.auth.exception.InvalidRefreshTokenException;
import com.backend.arthere.auth.exception.RefreshTokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRefreshToken(final RuntimeException error) {
        ErrorResponse errorResponse = new ErrorResponse(error.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValid(final MethodArgumentNotValidException error) {

        StringBuilder sb = new StringBuilder();
        error.getBindingResult().getAllErrors().forEach(e -> sb.append(e.getDefaultMessage())
                .append(System.lineSeparator()));

        ErrorResponse errorResponse = new ErrorResponse(sb.toString());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(final RuntimeException error) {
        ErrorResponse errorResponse = new ErrorResponse(error.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}