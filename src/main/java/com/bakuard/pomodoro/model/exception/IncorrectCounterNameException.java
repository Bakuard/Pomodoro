package com.bakuard.pomodoro.model.exception;

public class IncorrectCounterNameException extends AbstractDomainException {

    public IncorrectCounterNameException(String message, String messageKey) {
        super(message, messageKey);
    }

    public IncorrectCounterNameException(String message, String messageKey, boolean internalServerException) {
        super(message, messageKey, internalServerException);
    }

    public IncorrectCounterNameException(String message, Throwable cause, String messageKey, boolean internalServerException) {
        super(message, cause, messageKey, internalServerException);
    }

}
