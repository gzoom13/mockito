/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.util.HashMap;
import java.util.Map;

/**
 * Helps to get wrapper for corresponding non-void primitive type.
 */
public final class PrimitiveWrappers {

    private static final Map<Class<?>, Class<?>> WRAPPERS = new HashMap<>();

    static {
        WRAPPERS.put(boolean.class, Boolean.class);
        WRAPPERS.put(byte.class, Byte.class);
        WRAPPERS.put(short.class, Short.class);
        WRAPPERS.put(char.class, Character.class);
        WRAPPERS.put(int.class, Integer.class);
        WRAPPERS.put(long.class, Long.class);
        WRAPPERS.put(float.class, Float.class);
        WRAPPERS.put(double.class, Double.class);
    }

    private PrimitiveWrappers() {}

    /**
     * Returns wrapper class for specified non-void primitive class, or {@code null}
     * if key is not primitive or {@link Void#TYPE}.
     */
    public static Class<?> get(Class<?> key) {
        return WRAPPERS.get(key);
    }

    /**
     * Returns wrapper class for specified non-void primitive class, or {@code defaultValue}
     * if key is not primitive or {@link Void#TYPE}.
     */
    public static Class<?> getOrDefault(Class<?> key, Class<?> defaultValue) {
        return WRAPPERS.getOrDefault(key, defaultValue);
    }
}
