package com.sky.user.infrastructure.adapter.input.rest.error;

import com.sky.project.domain.exception.ProjectNotFoundException;
import com.sky.user.domain.exception.UserNotFoundException;
import com.sky.user.domain.exception.UsernameAlreadyTakenException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problem.setTitle("User not found");
		problem.setType(URI.create("about:blank"));
		return problem;
	}

	@ExceptionHandler(ProjectNotFoundException.class)
	public ProblemDetail handleProjectNotFound(ProjectNotFoundException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problem.setTitle("Project not found");
		problem.setType(URI.create("about:blank"));
		return problem;
	}

	@ExceptionHandler(UsernameAlreadyTakenException.class)
	public ProblemDetail handleUsernameTaken(UsernameAlreadyTakenException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
		problem.setTitle("Username already taken");
		problem.setType(URI.create("about:blank"));
		return problem;
	}

	/**
	 * Covers rare races where two requests create the same username before either
	 * commit (unique constraint still wins).
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
		String msg = ex.getMostSpecificCause().getMessage();
		if (msg != null && msg.contains("tb_user_username_key")) {
			ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Username already taken");
			problem.setTitle("Username already taken");
			problem.setType(URI.create("about:blank"));
			return problem;
		}
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
				ex.getMostSpecificCause().getMessage());
		problem.setTitle("Data constraint violation");
		problem.setType(URI.create("about:blank"));
		return problem;
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, org.springframework.http.HttpStatusCode status, WebRequest request) {
		Map<String, String> errors = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request validation failed");
		problem.setTitle("Validation failed");
		problem.setType(URI.create("about:blank"));
		problem.setProperty("errors", errors);
		return ResponseEntity.badRequest().body(problem);
	}
}
