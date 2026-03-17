package org.leaf.utils;

@FunctionalInterface
public interface DataCollector<T> {
    void collect(T data);
}
