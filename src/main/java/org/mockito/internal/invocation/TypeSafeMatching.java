/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.util.reflection.PrimitiveWrappers;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TypeSafeMatching implements ArgumentMatcherAction {

    private static final ArgumentMatcherAction TYPE_SAFE_MATCHING_ACTION = new TypeSafeMatching();

    private TypeSafeMatching() {}

    public static ArgumentMatcherAction matchesTypeSafe() {
        return TYPE_SAFE_MATCHING_ACTION;
    }

    @Override
    public boolean apply(ArgumentMatcher matcher, Object argument) {
        if (argument == null) return matcher.matches(argument);

        Class<?> expectedArgumentType = getArgumentType(matcher);

        if (expectedArgumentType.isInstance(argument)) {
            return matcher.matches(argument);
        } else if (shouldBePutInArray(matcher, expectedArgumentType, argument)) {
            Object argumentInArray = putArgumentInArray(argument, expectedArgumentType.getComponentType());
            return matcher.matches(argumentInArray);
        } else {
            return false;
        }
    }

    /**
     * Checks if argument can be put in array and tested by matcher.
     */
    private boolean shouldBePutInArray(ArgumentMatcher matcher, Class<?> expectedArgumentType, Object argument) {
        if (matcher instanceof VarargMatcher && expectedArgumentType.isArray()) {
            Class<?> componentType = expectedArgumentType.getComponentType();
            return PrimitiveWrappers.getOrDefault(componentType, componentType).isInstance(argument);
        }
        return false;
    }

    /**
     * Creates an array with single {@code argument} element.
     *
     * @param argument single element to put in array
     * @param arrayElementType type of array element
     * @return Array with single {@code argument} element
     */
    private Object putArgumentInArray(Object argument, Class<?> arrayElementType) {
        Object array = Array.newInstance(arrayElementType, 1);
        Array.set(array, 0, argument);
        return array;
    }

    /**
     * Returns the type of {@link ArgumentMatcher#matches(Object)} of the given
     * {@link ArgumentMatcher} implementation.
     */
    private static Class<?> getArgumentType(ArgumentMatcher<?> argumentMatcher) {
        Method[] methods = argumentMatcher.getClass().getMethods();

        for (Method method : methods) {
            if (isMatchesMethod(method)) {
                return method.getParameterTypes()[0];
            }
        }
        throw new NoSuchMethodError(
                "Method 'matches(T)' not found in ArgumentMatcher: "
                        + argumentMatcher
                        + " !\r\n Please file a bug with this stack trace at: https://github.com/mockito/mockito/issues/new ");
    }

    /**
     * Returns <code>true</code> if the given method is
     * {@link ArgumentMatcher#matches(Object)}
     */
    private static boolean isMatchesMethod(Method method) {
        if (method.getParameterTypes().length != 1) {
            return false;
        }
        if (method.isBridge()) {
            return false;
        }
        return "matches".equals(method.getName());
    }
}
