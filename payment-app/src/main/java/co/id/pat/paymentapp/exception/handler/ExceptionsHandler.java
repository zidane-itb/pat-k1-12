package co.id.pat.paymentapp.exception.handler;

import co.id.pat.paymentapp.dto.ErrorResponse;
import co.id.pat.paymentapp.exception.BadRequestException;
import co.id.pat.paymentapp.exception.InternalErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalError(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("Internal server error.");

        return ResponseEntity.internalServerError().body(errorResponse);
    }

}