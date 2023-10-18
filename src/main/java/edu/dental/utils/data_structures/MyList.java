package edu.dental.utils.data_structures;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * The data structure class. The dynamic resizable array. Implements {@link Collection} and {@link Serializable}.
 * It has the ability to {@linkplain MyList#searchElement(String, String) search}
 *  by a sample string in the desired field specified by the getter method.
 * @param <E> The type of elements in this list.
 */
public class MyList<E> implements Collection<E>, Serializable {

    private E[] elements;

    private static final int DEFAULT_CAPACITY = 30;

    private int size;

    public MyList() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public MyList(int capacity) {
        elements = (E[]) new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        E e = (E) o;
        return indexOf(e) >= 0;

    }

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index {@code i} such that
     * {@code Objects.equals(o, get(i))},
     * or -1 if there is no such index.
     */
    public int indexOf(E element) {
        if (element != null) {
            for (int i = 0; i < (elements.length - 1); i++) {
                if (element.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Returns the element at the specified position in this list.
     * @param  index The index of the element to return.
     * @return The element at the specified position in this list.
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E get(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("The index " + index +
                    " is out of the array bounds - the size is " + size);
        }
        return elements[index];
    }

    @Override
    public E[] toArray() {
        if (size == 0) {
            throw new NullPointerException("This collection object is empty.");
        }
        return Arrays.copyOf(elements, size);
    }

    @SuppressWarnings("all")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        return a;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("The specified element is null.");
        }
        if (size == elements.length) {
            elements = grow(elements);
        }
        elements[size] = element;
        size += 1;
        return true;
    }

    /**
     * Add the specified element to the specified position of this list.
     * If the elements are present later the given position index,
     *  the array moving, nothing is removing.
     * @param index The wanted position for inserting an element.
     * @param e Specified element to be added to this list.
     * @return {@code true} if it was successful.
     */
    public boolean add(int index, E e) {
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException("The index " + index +
                        " is out of the array bounds - the size is " + size);
        }
        if (e == null) {
            throw new NullPointerException("The specified element is null.");
        }
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) new Object[index];
        System.arraycopy(elements, 0, arr, 0, index);
        elements[index] = e;
        System.arraycopy(arr, 0, elements, index + 1, arr.length);
        return true;
    }

    /**
     * Add the specified element to the specified position of this list,
     *  replacing the element that are present in the given position index.
     * @param index The wanted position for inserting an element.
     * @param e Specified element to be added to this list.
     * @return {@code true} if it was successful.
     */
    public boolean replace(int index, E e) {
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException("The index " + index +
                    " is out of the array bounds - the size is " + size);
        }
        if (e == null) {
            throw new NullPointerException("The specified element is null.");
        }
        elements[index] = e;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException("The specified element is null.");
        }
        @SuppressWarnings("unchecked")
        E e = (E) o;
        for (int i = 0; i < size; i++)
            if (e.equals(elements[i])) {
                fastRemove(i);
                break;
            }
        return true;
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     * @param index The index of the element to be removed.
     * @return True if it was successful.
     * @throws IndexOutOfBoundsException If the given index is out of this list bounds.
     */
    public boolean remove(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("The index " + index +
                    " is out of the array bounds - the size is " + size);
        }
        fastRemove(index);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("The specified object is null.");
        }
        for (Object o : c) {
            if (!(contains(o))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("The specified object is null.");
        }
        for (E e : c) {
            if (!(contains(e))) {
                add(e);
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("The specified object is null.");
        }
        for (int i = 0; i < size; i++) {
            E e = elements[i];
            if (c.contains(e)) {
                remove(e);
            }
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("The specified object is null.");
        }
        for (int i = 0; i < size; i++) {
            E e = elements[i];
            if (!(c.contains(e))) {
                remove(e);
                i--;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        size = 0;
        elements = (E[]) new Object[elements.length];
    }

    @Override
    public MyIterator iterator() {
        return new MyIterator();
    }

    /**
     * Search for an object in this {@link MyList} by specified field, using the string equaling with a given sample.
     * @param field Field of the searchable object for equaling.
     * @param sample A sample value to search the element.
     * @return  The {@link MyList list} of the relevant objects.
     * @throws NoSuchElementException If something goes wrong.
     * @throws IllegalAccessException If something goes wrong.
     */
    @SuppressWarnings("unused")
    public MyList<E> searchElement(String field, String sample) throws NoSuchFieldException, IllegalAccessException {
        if (field == null || field.isEmpty()) {
            throw new NullPointerException("The field argument is null or empty.");
        }
        if (sample == null || sample.isEmpty()) {
            throw new NullPointerException("The sample argument is null or empty.");
        }
        Searcher searcher = new Searcher();
        searcher.search(field, sample);
        if (searcher.relevant.isEmpty()) {
            throw new NoSuchElementException("The specified elements is not found.");
        }
        return searcher.relevant;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (size != ((MyList<?>) o).size) {
            return false;
        }
        for (Object e : (MyList<?>)o) {
            if (!(this.contains(e))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        Object[] arr = Arrays.copyOf(elements, size);
        return Arrays.toString(arr);
    }

    private E[] grow(E[] arr) {
        @SuppressWarnings("unchecked")
        E[] result = (E[]) new Object[size << 1];
        System.arraycopy(arr, 0, result, 0, arr.length);
        return result;
    }

    private void fastRemove(int i) {
        size -= 1;
        elements[i] = null;
        if (size > i) {
            System.arraycopy(elements, i + 1, elements, i, size - i);
        }
    }


    private class MyIterator implements Iterator<E> {


        private int step;

        private int n;

        private MyIterator() {
            this.step = 0;
            this.n = 0;
        }

        @Override
        public boolean hasNext() {
            return (this.step < size);
        }

        @Override
        public E next() {
            this.n = this.step;
            if (this.step >= size) {
                throw new NoSuchElementException();
            }
            this.step += 1;
            return elements[n];
        }
    }

    /**
     *  The class used by the {@linkplain MyList#searchElement(String, String) search method}.
     */
    private class Searcher {

        private final MyList<E> relevant;

        private Searcher() {
            this.relevant = new MyList<>();
        }

        private Object getFieldValue(String field, E e) throws NoSuchFieldException, IllegalAccessException {
            Field fieldOfElement = e.getClass().getDeclaredField(field);
            fieldOfElement.setAccessible(true);
            Object fieldValue = fieldOfElement.get(e);
            fieldOfElement.setAccessible(false);
            return fieldValue;
        }

        private void search(String field, String sample) throws NoSuchFieldException, IllegalAccessException {
            for (E e : elements) {
                if (e == null) {
                    break;
                }
                //get string value of object field.
                String value = String.valueOf(getFieldValue(field, e));
                if (value.equalsIgnoreCase(sample)) {
                    this.relevant.add(e);
                }
            }
        }
    }

}