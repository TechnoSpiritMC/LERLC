package org.leaf.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
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

        try {
            return list.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public T peek() {
        ensureInitialization();

        try {
            return list.getLast();
        } catch (NoSuchElementException e) {
            return null;
        }
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

    @Override
    public String toString() {
        return list.toString();
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
