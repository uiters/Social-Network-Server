package uit.core.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ExceptionResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date timestamp;
    private String code;
    private String message;

    public ExceptionResponse(Date timestamp, String code, String message) {
        this.timestamp = timestamp;
        this.code = code;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
