package com.seph_worker.worker.core.exception;

import lombok.experimental.StandardException;

@StandardException
public class ResourceNotFoundException extends RuntimeException{ public ResourceNotFoundException() {
    this((String)null, (Throwable)null);
}

    public ResourceNotFoundException(final String message) {
        this(message, (Throwable)null);
    }

    public ResourceNotFoundException(final Throwable cause) {
        this(cause != null ? cause.getMessage() : null, cause);
    }

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message);
        if (cause != null) {
            super.initCause(cause);
        }

    }
}
