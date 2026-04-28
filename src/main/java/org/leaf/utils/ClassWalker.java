package org.leaf.utils;

// License & Attribution.
// Please note that this piece of software was greatly inspired by the amazing work of
// the Discord JDA Team for their ClassWalker class.

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ClassWalker implements Iterable<Class<?>> {
    private final Class<?> clazz;
    private final Class<?> end;

    private ClassWalker(Class<?> clazz) {
        this(clazz, Object.class);
    }

    private ClassWalker(Class<?> clazz, Class<?> end) {
        this.clazz = clazz;
        this.end = end;
    }

    public static ClassWalker range(Class<?> start, Class<?> end) {
        return new ClassWalker(start, end);
    }

    public static ClassWalker walk(Class<?> start) {
        return new ClassWalker(start);
    }

    @NotNull
    @Override
    public Iterator<Class<?>> iterator() {
        return new Iterator<>() {
            private final Set<Class<?>> done = new HashSet<>();
            private final Deque<Class<?>> work = new LinkedList<>();

            {
                work.addLast(clazz);
                done.add(end);
            }

            @Override
            public boolean hasNext() {
                return !work.isEmpty();
            }

            @Override
            public Class<?> next() {
                Class<?> current = work.removeFirst();
                done.add(current);
                for (Class<?> parent : current.getInterfaces()) {
                    if (!done.contains(parent)) {
                        work.addLast(parent);
                    }
                }

                Class<?> parent = current.getSuperclass();
                if (parent != null && !done.contains(parent)) {
                    work.addLast(parent);
                }
                return current;
            }
        };
    }
}
