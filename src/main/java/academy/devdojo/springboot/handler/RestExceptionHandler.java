package academy.devdojo.springboot.handler;

import academy.devdojo.springboot.exception.BadRequestException;
import academy.devdojo.springboot.exception.BadRequestExceptionDetails;
import academy.devdojo.springboot.exception.ExceptionDetails;
import academy.devdojo.springboot.exception.ValidationExceptionDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handle(BadRequestException bre){

        return  new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception")
                        .details(bre.getMessage())
                        .developerMessage(bre.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String collect = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
        String message = fieldErrors.stream().map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));


         return  new ResponseEntity<>(
                 ValidationExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception. Invalid Fields")
                        .details(ex.getMessage())
                        .developerMessage(ex.getClass().getName())
                        .fields(collect)
                        .fieldsMessage(message)
                        .build(), HttpStatus.BAD_REQUEST
        );

    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body,
                                                             HttpHeaders headers, HttpStatus status,
                                                             WebRequest request) {
        ExceptionDetails build = ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title(ex.getCause().getMessage())
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .build();

        return new ResponseEntity(build, headers, status);
    }

}
