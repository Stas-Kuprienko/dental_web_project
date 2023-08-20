package dental.app;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

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

    @Override
    public boolean contains(Object o) {
        try {
            E e = (E) o;
            return indexOf(e) >= 0;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
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

    @SuppressWarnings("unchecked")
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
        if (size == elements.length) {
            elements = grow(elements);
        }
        elements[size] = element;
        size += 1;
        return true;
    }

    private E[] grow(E[] arr) {
        @SuppressWarnings("unchecked")
        E[] result = (E[]) new Object[size << 1];
        System.arraycopy(arr, 0, result, 0, arr.length);
        return result;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            new NullPointerException().printStackTrace();
            return false;
        }
        try {
            E e = (E) o;
            for (int i = 0; i < size; i++)
                if (e.equals(elements[i])) {
                    fastRemove(i);
                    break;
                }
            return true;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void fastRemove(int i) {
        size -= 1;
        if (size > i) {
            System.arraycopy(elements, i + 1, elements, i, size - i);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        try {
            for (Object o : c) {
                if (!(contains(o))) {
                    return false;
                }
            } return true;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        try {
            @SuppressWarnings("unchecked")
            MyList<E> list = (MyList<E>) c;
            for (E e : list) {
                if (!(add(e))) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        try {
            @SuppressWarnings("unchecked")
            MyList<E> list = (MyList<E>) c;
            for (E e : list) {
                if (!(remove(e))) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        try {
            @SuppressWarnings("unchecked")
            MyList<E> list = (MyList<E>) c;
            for (E e : elements) {
                if (!(list.contains(e))) {
                    remove(e);
                }
            }
            return true;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        size = 0;
        elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    @Override
    public String toString() {
        Object[] arr = Arrays.copyOf(elements, size);
        return Arrays.toString(arr);
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

    class Searcher {

        private final MyList<E> relevant;

        Searcher() {

            this.relevant = new MyList<>();
        }

        public void search(String sequence) {
            int i = 0, f = 0;
            for (; i < size; i++) {
                if (elements[i].toString().contains(sequence)) {
                    this.relevant.add(elements[i]);
                    f++;
                }
            }
        }

        public MyList<E> getRelevant() {
            return this.relevant;
        }

    }

}