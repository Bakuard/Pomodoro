package com.bakuard.pomodoro.model.exception;

public class NoSuchCounterException extends AbstractDomainException {

    public NoSuchCounterException(String message, String messageKey) {
        super(message, messageKey);
    }

    public NoSuchCounterException(String message, String messageKey, boolean internalServerException) {
        super(message, messageKey, internalServerException);
    }

    public NoSuchCounterException(String message, Throwable cause, String messageKey, boolean internalServerException) {
        super(message, cause, messageKey, internalServerException);
    }

}
