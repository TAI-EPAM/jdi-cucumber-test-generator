package com.epam.test_generator.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionController {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Void> handleRunTimeException(Exception ex) {
		System.out.println(ex.getClass());
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		Map<String, String> result = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach((f)->
					result.put(f.getField(),f.getDefaultMessage()));

		return new ResponseEntity<>(result,HttpStatus.UNPROCESSABLE_ENTITY);
	}


}