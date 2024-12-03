package com.bupt.exception;

public class ThrowException extends  Exception{
    public ThrowException() {
        super();
    }

    public ThrowException(String message) {
        super(message);
    }

    public ThrowException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThrowException(Throwable cause) {
        super(cause);
    }
}
