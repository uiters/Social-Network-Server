package uit.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
public class ErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> illegalArgument(RuntimeException ex) {

        ExceptionResponse response = new ExceptionResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                ex.getMessage());

        logger.info("Returning HTTP " + HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> illegalArgument(Exception ex) {

        ExceptionResponse response = new ExceptionResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                ex.getMessage());

        logger.info("Returning HTTP " + HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }
}
