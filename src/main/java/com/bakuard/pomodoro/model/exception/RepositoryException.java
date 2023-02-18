package com.bakuard.pomodoro.model.exception;

public class RepositoryException extends AbstractDomainException {

    public RepositoryException(String message, String messageKey) {
        super(message, messageKey);
    }

    public RepositoryException(String message, String messageKey, boolean internalServerException) {
        super(message, messageKey, internalServerException);
    }

    public RepositoryException(String message, Throwable cause, String messageKey, boolean internalServerException) {
        super(message, cause, messageKey, internalServerException);
    }

}
