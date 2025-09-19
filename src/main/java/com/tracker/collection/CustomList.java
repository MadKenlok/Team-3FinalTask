package com.tracker.collection;

import java.util.*;
import java.util.stream.Stream;

public class CustomList<T> implements Iterable<T> {
    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public CustomList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public CustomList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Емкость не может быть отрицательной");
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }

    public void add(T element) {
        ensureCapacity(size+1);
        elements[size++] = element;
    }

    public void addAll(CustomList<? extends T> other) {
        if (other.size == 0) return;
        ensureCapacity(size + other.size);
        System.arraycopy(other.elements, 0, elements, size, other.size);
        size += other.size;
    }


    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", размер: " + size);
        }
        return (T) elements[index];
    }

    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", размер: " + size);
        }
        elements[index] = element;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    public T[] toArray() {
        return (T[]) Arrays.copyOf(elements, size);
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator();
    }

    private class CustomIterator implements Iterator<T> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (T) elements[currentIndex++];
        }
    }

    public Stream<T> stream() {
        return Arrays.stream(toArray());
    }
}