package com.bakuard.pomodoro.model.exception;

public class IncorrectTimeException extends AbstractDomainException {

    public IncorrectTimeException(String message, String messageKey) {
        super(message, messageKey);
    }

    public IncorrectTimeException(String message, String messageKey, boolean internalServerException) {
        super(message, messageKey, internalServerException);
    }

    public IncorrectTimeException(String message, Throwable cause, String messageKey, boolean internalServerException) {
        super(message, cause, messageKey, internalServerException);
    }

}
