package org.leaf.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Stack <T> implements Iterable<T>{

    List<T> list = new ArrayList<>();
    int capacity;

    public Stack(int capacity) {
        this.capacity = capacity;
    }

    public Stack() {
        this(-1);
    }

    public void setSize(int size) {
        this.capacity = size;
    }

    void ensureInitialization() {
        assert capacity != -1;
    }

    public void addAll(List<T> list) {
        ensureInitialization();

        this.list.addAll(list);
        shiftAll();
    }

    public void push(T t) {
        ensureInitialization();

        list.add(t);
        shiftAll();
    }

    public T pop() {
        ensureInitialization();

        return list.removeLast();
    }

    public List<T> getAsList() {
        ensureInitialization();

        return List.copyOf(list);
    }

    public List<T> get() {
        ensureInitialization();

        return list;
    }

    public T peek(int index) {
        ensureInitialization();

        return list.get(index);
    }

    public T peek() {
        ensureInitialization();

        return list.getLast();
    }

    private void shiftAll() {
        ensureInitialization();

        while (list.size() > capacity) {
            list.removeFirst();
        }
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return list.iterator();
    }

    public Stream<T> stream() {
        return list.stream();
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void clear() {
        list.clear();
    }
}
