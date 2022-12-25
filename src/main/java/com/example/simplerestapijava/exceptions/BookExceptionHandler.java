package com.example.simplerestapijava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.simplerestapijava.ResponseTransfer;

@ControllerAdvice
public class BookExceptionHandler {
    @ResponseBody
	@ExceptionHandler(BookConflictException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	ResponseTransfer BookConflictHandler(BookConflictException ex) {
		return new ResponseTransfer(ex.getMessage());
	}
}
