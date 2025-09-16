package com.example.clinic.web;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApiExceptionHandler {

@ExceptionHandler(IllegalArgumentException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseBody
public Map<String,Object> handleBadRequest(IllegalArgumentException ex) {
 return Map.of("error", "bad_request", "message", ex.getMessage());
}

@ExceptionHandler(IllegalStateException.class)
@ResponseStatus(HttpStatus.UNAUTHORIZED)
@ResponseBody
public Map<String,Object> handleUnauthorized(IllegalStateException ex) {
 return Map.of("error", "unauthorized", "message", ex.getMessage());
}

@ExceptionHandler(DataIntegrityViolationException.class)
@ResponseStatus(HttpStatus.CONFLICT)
@ResponseBody
public Map<String,Object> handleConstraint(DataIntegrityViolationException ex) {
 return Map.of("error", "constraint_violation", "message", ex.getMostSpecificCause().getMessage());
}
}
