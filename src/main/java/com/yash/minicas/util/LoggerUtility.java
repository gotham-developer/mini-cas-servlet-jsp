package com.yash.minicas.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides a simple and consistent way to obtain Log4j2 {@link Logger} instances.
 * <p>
 * This is a non-instantiable utility class designed to centralize logger creation
 * and ensure consistent logging configuration across the application.
 * </p>
 *
 * <h5>Usage Example:</h5>
 * <pre>{@code
 * private static final Logger logger = LoggerUtility.getLogger(MyClass.class);
 * }</pre>
 *
 * @see org.apache.logging.log4j.LogManager
 * @see org.apache.logging.log4j.Logger
 * @since 1.0.0
 */
public final class LoggerUtility {
    /**
     * Private constructor to prevent instantiation.
     */
    private LoggerUtility() {
        throw new UnsupportedOperationException("LoggerUtility is a utility class and cannot be instantiated.");
    }

    /**
     * Returns a Log4j2 {@link Logger} instance associated with the specified class.
     *
     * @param clazz the class for which the logger should be created; must not be {@code null}
     *
     * @return a {@link Logger} instance tied to the given class
     *
     * @throws IllegalArgumentException if {@code clazz} is {@code null}
     */
    public static Logger getLogger(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class reference cannot be null.");
        }
        return LogManager.getLogger(clazz);
    }
}
