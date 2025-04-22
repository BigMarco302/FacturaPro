package com.seph_worker.worker.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seph_worker.worker.core.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ControllerAdvice
@Schema(description = "Generic response object used in all API requests.")
public class WebServiceResponse {


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody//, WebRequest request
    public WebServiceResponse handleResourceNotFoundException(ResourceNotFoundException e) {
        return new WebServiceResponse(false, e.getMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public WebServiceResponse handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return new WebServiceResponse(false, "El header '"+ e.getHeaderName() +"' es necesario.");
    }
    @Schema(
            description = "Indicates whether the request was processed successfully or not.",
            required = true
    )
    private boolean success;
    @Schema(
            description = "Contains an optional message from the executed service."
    )
    private String message;
    @Schema(
            description = "Contains the data that will be returned to the client."
    )
    private Object data;
    private Integer count;

    private WebServiceResponse() {
    }

    public WebServiceResponse(boolean success, String message, List<?> list) {
        this.success = success;
        this.message = message;
        this.data = list;
    }

    public WebServiceResponse(boolean success, String message, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public WebServiceResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public WebServiceResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public WebServiceResponse(boolean success) {
        this.success = success;
        this.message = success ? "OK" : "Error";
    }

    public WebServiceResponse(boolean success, String message, String key, Object value) {
        this.success = success;
        this.message = message;
        this.data = Collections.singletonMap(key, value);
    }

    public WebServiceResponse(Object data) {
        this.success = true;
        this.message = "OK";
        this.data = data;
    }

    public Object getData() {
        if (this.data == null) {
            this.data = new HashMap();
        }

        return this.data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public WebServiceResponse(Object data, Integer count) {
        this.success = true;
        this.message = "OK";
        this.data = data;
        this.count = count;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setCount(final Integer count) {
        this.count = count;
    }
}

