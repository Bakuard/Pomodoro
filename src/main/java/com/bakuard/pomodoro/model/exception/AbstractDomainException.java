package com.bakuard.pomodoro.model.exception;

public abstract class AbstractDomainException extends RuntimeException {

    private final String messageKey;
    private final boolean internalServerException;

    public AbstractDomainException(String message, String messageKey) {
        super(message);
        this.messageKey = messageKey;
        this.internalServerException = false;
    }

    public AbstractDomainException(String message, String messageKey, boolean internalServerException) {
        super(message);
        this.messageKey = messageKey;
        this.internalServerException = internalServerException;
    }

    public AbstractDomainException(String message, Throwable cause, String messageKey, boolean internalServerException) {
        super(message, cause);
        this.messageKey = messageKey;
        this.internalServerException = internalServerException;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public boolean isInternalServerException() {
        return internalServerException;
    }

    public boolean isUserLevelException() {
        return !internalServerException;
    }

}
