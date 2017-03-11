package com.github.privacystreams.utils;

/**
 * A helper class to get access to assertion-related utilities.
 */
public final class Assertions {
    /**
     * Throw IllegalArgumentException if the value is null.
     *
     * @param name  the parameter name
     * @param value the value that should not be null
     * @param <T>   the value type
     * @return the value
     * @throws java.lang.IllegalArgumentException if value is null
     */
    public static <T> T notNull(final String name, final T value) {
        if (value == null) {
            throw new IllegalArgumentException(name + " can not be null");
        }
        return value;
    }

    /**
     * Throw IllegalStateException if the condition is false.
     *
     * @param name      the name of the state that is being checked
     * @param condition the condition about the parameter to check
     * @throws java.lang.IllegalStateException if the condition is false
     */
    public static void isTrue(final String name, final boolean condition) {
        if (!condition) {
            throw new IllegalStateException("state should be: " + name);
        }
    }

    /**
     * Cast an object to T.
     * Throw IllegalArgumentException if the the value is not castable.
     *
     * @param name      the parameter name
     * @param value     the value to cast from
     * @param <T>       the type to cast to
     * @throws java.lang.IllegalArgumentException if the value is not
     */
    public static <T> T cast(final String name, final Object value) {
        try {
            return (T) value;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(name + " cannot be casted: " + value.getClass().getName());
        }
    }

    private Assertions() {
    }
}
