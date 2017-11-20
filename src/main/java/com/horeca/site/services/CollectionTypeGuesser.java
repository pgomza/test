package com.horeca.site.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CollectionTypeGuesser {

    public static Class<?> guessElementType(Collection<?> collection) {
        Class<?> guess = null;
        for (Object element : collection) {
            if (element != null) {
                if (guess == null) {
                    guess = element.getClass();
                }
                else if (guess != element.getClass()) {
                    guess = lowestCommonSuper(guess, element.getClass());
                }
            }
        }
        return guess;
    }

    private static Set<Class<?>> supers(Class<?> c) {
        if (c == null) {
            return new HashSet<>();
        }

        Set<Class<?>> s = supers(c.getSuperclass());
        s.add(c);
        return s;
    }

    private static Class<?> lowestCommonSuper(Class<?> a, Class<?> b) {
        Set<Class<?>> aSupers = supers(a);
        while (!aSupers.contains(b)) {
            b = b.getSuperclass();
        }
        return b;
    }
}
