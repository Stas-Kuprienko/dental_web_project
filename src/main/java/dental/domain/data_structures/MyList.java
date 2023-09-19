package dental.domain.data_structures;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The data structure class. The dynamic resizable array. Implements {@link Collection} and {@link Serializable}.
 * It has the ability to {@linkplain MyList#searchByString(Searchable, String, Class) search} by a sample string in the desired field specified by the getter method.
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

    public E get(int index) {
        return elements[index];
    }

    @Override
    public E[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @SuppressWarnings("all")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(a, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        return a;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NoSuchElementException();
        }
        if (size == elements.length) {
            elements = grow(elements);
        }
        elements[size] = element;
        size += 1;
        return true;
    }

    public boolean add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException();
        }
        if (element == null) {
            throw new NullPointerException();
        }
        elements[index] = element;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
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

    public boolean remove(int index) {
        fastRemove(index);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!(contains(o))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            if (!(contains(e))) {
                add(e);
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
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
     * Search for an object in {@link MyList} using the String comparing with a getter value.
     * @param getter A getter method that returns String value of the searchable object.
     * @param sample A sample String value for search.
     * @param clas   A class of searchable object.
     * @return  The {@link MyList list} of the relevant objects.
     * @throws Exception If something goes wrong.
     */
    @SuppressWarnings("rawtypes")
    public MyList<E> searchByString(String field, String sample, Class clas) throws Exception {
        Searcher searcher = new Searcher(clas);
        searcher.search(field, sample);
        return searcher.relevant;
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
     *  The class used by the {@linkplain MyList#searchByString(String, String, Class) search method}.
     */
    @SuppressWarnings("all")
    private class Searcher {

        private final MyList<E> relevant;

        // the class type of the searchable object.
        private final Class clas;

        private Searcher(Class clas) {
            this.relevant = new MyList<>();
            this.clas = (clas);
        }

        private Object invokeMethod(String field, E e) throws Exception {
            char c = (char) (field.charAt(0) - ' ');
            String getter = "get" + c + field.substring(1, field.length());
            //get the method of the searched object, used to get a comparable value,
            // then invoke this method and return result.
            try {
                return (String) clas.getMethod(getter).invoke(e, null);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                throw new Exception(exception.getCause());
            }
        }

        private void search(String field, String sample) throws Exception {
            for (E e : elements) {
                if (e == null) {
                    break;
                }
                //get string value of object getter method.
                String s = (String) invokeMethod(field, e);
                if (s.equalsIgnoreCase(sample)) {
                    this.relevant.add(e);
                }
            }
        }
    }

}