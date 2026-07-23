package org.leaf.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ObservableValue<T> {
    private T value;
    private DataCollector<T> onChange;
    private final Map<String, FieldWatcher> fieldWatchers = new HashMap<>();
    private Thread watcherThread;
    private volatile boolean watching = false;

    public ObservableValue(T initial) {
        this.value = initial;
        if (initial != null && isPrimitive(initial.getClass())) {
            startFieldWatching();
        }
    }

    public void set(T newValue) {
        if (!Objects.equals(value, newValue)) {
            stopFieldWatching();
            value = newValue;
            if (newValue != null && isPrimitive(newValue.getClass())) {
                startFieldWatching();
            }
            if (onChange != null) onChange.collect(newValue);
        }
    }

    public T get() {
        return value;
    }

    public void setOnChange(DataCollector<T> listener) {
        this.onChange = listener;
    }

    private void startFieldWatching() {
        if (value == null) return;

        watching = true;
        fieldWatchers.clear();

        for (Field field : value.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(value);
                fieldWatchers.put(field.getName(), new FieldWatcher(field, fieldValue));
            } catch (IllegalAccessException e) {
                // Skip inaccessible fields
            }
        }

        watcherThread = new Thread(() -> {
            while (watching) {
                try {
                    checkFieldChanges();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        watcherThread.setDaemon(true);
        watcherThread.start();
    }

    private void stopFieldWatching() {
        watching = false;
        if (watcherThread != null) {
            watcherThread.interrupt();
            watcherThread = null;
        }
    }

    private void checkFieldChanges() {
        if (value == null) return;

        for (Map.Entry<String, FieldWatcher> entry : fieldWatchers.entrySet()) {
            FieldWatcher watcher = entry.getValue();
            try {
                Object currentValue = watcher.field.get(value);
                if (!Objects.equals(watcher.lastValue, currentValue)) {
                    watcher.lastValue = currentValue;
                    if (onChange != null) onChange.collect(value);
                }
            } catch (IllegalAccessException e) {
                // Skip inaccessible fields
            }
        }
    }

    private boolean isPrimitive(Class<?> clazz) {
        return !clazz.isPrimitive() &&
                clazz != String.class &&
                clazz != Integer.class &&
                clazz != Long.class &&
                clazz != Double.class &&
                clazz != Float.class &&
                clazz != Boolean.class &&
                clazz != Character.class &&
                clazz != Byte.class &&
                clazz != Short.class;
    }

    private static class FieldWatcher {
        final Field field;
        Object lastValue;

        FieldWatcher(Field field, Object lastValue) {
            this.field = field;
            this.lastValue = lastValue;
        }
    }
}