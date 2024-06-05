package com.pgimenez.test.api.exception;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pgimenez.test.api.dto.ApiExceptionResponse;

/**
 *
 * Standard http communication have five levels of response codes
 * 100-level (Informational) — Server acknowledges a request, it mean that request was received and understood, it is transient response , alert client for awaiting response
 * 200-level (Success) — Server completed the request as expected
 * 300-level (Redirection) — Client needs to perform further actions to complete the request
 * 400-level (Client error) — Client sent an invalid request
 * 500-level (Server error) — Server failed to fulfill a valid request due to an error with server
 * 
 */

@RestControllerAdvice//Indicate that this class assit a controller class and can have a body in response
public class ApiExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<ApiExceptionResponse> handleUnknownHostException(UnknownHostException ex) {
        log.debug("Exception unknown: ", ex.getCause());
        log.error("Exception unknown: {}", ex.getMessage());
        ApiExceptionResponse response = new ApiExceptionResponse(G100, G100_MESSAGE);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionResponse> handleException(Exception ex) {
        log.debug("Exception: ", ex.getCause());
        log.error("Exception: {}", ex.getMessage());
        ApiExceptionResponse response = new ApiExceptionResponse(G100, G100_MESSAGE);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiExceptionResponse> handleApiException(ApiException ex) {
        log.debug("Api exception: ", ex.getCause());
        log.error("Api exception: {}", ex.getMessage());
        ApiExceptionResponse response = new ApiExceptionResponse(ex.getCode(), getErrorMessageForErrorCode(ex.getCode()));
        return new ResponseEntity<>(response, getHttpStatusForErrorCode(ex.getCode()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        if (mostSpecificCause instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) mostSpecificCause;
            String fieldName = jsonMappingException.getPath().stream()
                .map(Reference::getFieldName)
                .reduce((first, second) -> first + ", " + second)
                .orElse("Unknown Field");
            String msgException = ex.getMessage();
            String msg = (msgException != null && msgException.contains("not one of the values accepted for Enum class"))
                ? "Valor no válido para el campo '" + fieldName + "'."
                : "Error de formato en el campo '" + fieldName + "'.";
            return new ResponseEntity<>(new ApiExceptionResponse(G100, msg), HttpStatus.BAD_REQUEST);
        }
        ApiExceptionResponse response = new ApiExceptionResponse(G100, "La solicitud contiene un formato no válido");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Retorna estado HTTP según el código del error
    private HttpStatus getHttpStatusForErrorCode(String errorCode) {
        switch(errorCode) {
            case G267:
                return HttpStatus.NOT_FOUND;
            case G268:
                return HttpStatus.BAD_REQUEST;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    // Retorna mensaje de error para el cliente, según el código del error
    private String getErrorMessageForErrorCode(String errorCode) {
        switch(errorCode) {
            case G267:
                return G267_MESSAGE;
            case G268:
                return G268_MESSAGE;
            default:
                return G100_MESSAGE;
        }
    }

    // Códigos de error
    public static final String G267 = "g267";
    public static final String G268 = "g268";
    public static final String G100 = "g100";

    // Mensaje de error
    public static final String G267_MESSAGE = "No se encuentran noticias para el texto de busqueda";
    public static final String G268_MESSAGE = "Parámetros inválidos";
    public static final String G100_MESSAGE = "Error interno del servidor";

}
