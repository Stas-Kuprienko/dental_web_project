package dental.app.records;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyList<E> implements Collection<E>, Serializable {

    private E[] elements;

    private static final int DEFAULT_CAPACITY = 30;

    private int size;

    MyList() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    MyList(int capacity) {
        this.elements = (E[]) new Object[capacity];
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
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

    int indexOf(E element) {
        if (element != null) {
            for (int i = 0; i < (this.elements.length - 1); i++) {
                if (element.equals(this.elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E[] toArray() {
        E[] result = (E[]) new Object[this.size - 1];
        System.arraycopy(this.elements, 0, result, 0, this.size - 1);
        return result;
    }

    @Override
    @Deprecated
    public <T> T[] toArray(T[] a) {
        if (a != null) {
            int len = Math.min(a.length, this.size);
            System.arraycopy(this.elements, 0, a, 0, len);
        }
        return a;
    }

    @Override
    public boolean add(E element) {
        if ((this.size - 1) == this.elements.length) {
            this.elements = grow(this.elements);
        }
        this.elements[this.size] = element;
        this.size += 1;
        return true;
    }

    private E[] grow(E[] arr) {
        @SuppressWarnings("unchecked")
        E[] result = (E[]) new Object[this.size << 1];
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
            for (int i = 0; i < this.size; i++)
                if (e.equals(this.elements[i])) {
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
        this.size -= 1;
        if (this.size > i) {
            System.arraycopy(this.elements, i + 1, this.elements, i, this.size - i);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        try {
            for (Object o : c) {
                if (!(this.contains(o))) {
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
                if (!(this.add(e))) {
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
                if (!(this.remove(e))) {
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
            for (E e : this.elements) {
                if (!(list.contains(e))) {
                    this.remove(e);
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
        this.size = 0;
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
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




