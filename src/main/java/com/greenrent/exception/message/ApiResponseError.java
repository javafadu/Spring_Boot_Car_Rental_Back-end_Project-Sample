package com.greenrent.exception.message;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

// Olusan exceptionlari throw ettigimiz zaman Client a gonderilecek message icin
// custom bir error class yazdik.
public class ApiResponseError {

    private HttpStatus status;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd-MM-yyyy hh:mm:ss" )
    private LocalDateTime timestamp;
    private String message;
    private String requestURI;

    private ApiResponseError() {
        timestamp=LocalDateTime.now();
    }


    public ApiResponseError(HttpStatus status) {
        this(); // private class i cagiriyoruz
        this.status=status;
    }

    public ApiResponseError(HttpStatus status,String message, String requestURI) {
        this(status);
        this.message=message;
        this.requestURI=requestURI;
    }
    public HttpStatus getStatus() {
        return status;
    }
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getRequestURI() {
        return requestURI;
    }
    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }



}