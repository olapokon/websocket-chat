package olapokon.websocketchat.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(MainExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    protected String handleValidationException(ValidationException ex, Model model) {
        log.debug("Validation failed", ex);
        model.addAttribute("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("errorReasonPhrase", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return "error-page";
    }

    @ExceptionHandler(InvalidStateException.class)
    protected String handleInvalidStateException(InvalidStateException ex, Model model) {
        log.debug("Invalid state", ex);
        model.addAttribute("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("errorReasonPhrase", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return "error-page";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        log.debug("Resource not found", ex);
        model.addAttribute("errorCode", HttpStatus.NOT_FOUND.value());
        model.addAttribute("errorReasonPhrase", HttpStatus.NOT_FOUND.getReasonPhrase());
        return "error-page";
    }
}
