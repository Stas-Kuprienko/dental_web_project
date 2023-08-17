package dental.app.records;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public class CollectRecords implements Collection<Record>, Serializable {

    private Record[] records;

    private int size;

    CollectRecords() {
        this.records = new Record[30];
    }

    class Searcher {

        private Record[] relevant;

        Searcher() {
            this.relevant = new Record[10];
        }

        public void search(String patient) {
            int i = 0, f = 0;
            for (; i < size; i++) {
                if (f >= this.relevant.length) {
                    this.relevant = grow(this.relevant);
                }
                if (records[i].getPatient().equalsIgnoreCase(patient)) {
                    this.relevant[f] = records[i];
                    f++;
                }
            }
        }

        public void search(String patient, String clinic) {
            int i = 0, f = 0;
            for (; i < size; i++) {
                if (f >= this.relevant.length) {
                    this.relevant = grow(this.relevant);
                }
                if ((records[i].getPatient().equalsIgnoreCase(patient))
                   && (records[i].getClinic().equalsIgnoreCase(clinic))) {
                        this.relevant[f] = records[i];
                        f++;
                }
            }
        }

        public Record[] getRelevant() {
            return this.relevant;
        }
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
            Record r = (Record) o;
            return indexOf(r) >= 0;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    int indexOf(Record r) {
        if (r != null) {
            for (int i = 0; i < (this.records.length - 1); i++) {
                if (r.equals(this.records[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public Iterator<Record> iterator() {
        //TODO
        return null;
    }

    @Override
    public Record[] toArray() {
        Record[] result = new Record[this.size - 1];
        System.arraycopy(this.records, 0, result, 0, this.size - 1);
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        //TODO
        return null;
    }

    @Override
    public boolean add(Record record) {
        if ((this.size - 2) >= this.records.length) {
            this.records = grow(this.records);
        }
        this.records[this.size] = record;
        this.size += 1;
        return true;
    }

    private Record[] grow(Record[] arr) {
        Record[] result = new Record[this.size + 15];
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
            Record r = (Record) o;
            for (int i = 0; i < this.size; i++)
                if (r.equals(this.records[i])) {
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
            System.arraycopy(this.records, i + 1, this.records, i, this.size - i);
        }
    }



    @Override
    public boolean containsAll(Collection<?> c) {
        //TODO
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Record> c) {
        //TODO
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        //TODO
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        //TODO
        return false;
    }

    @Override
    public void clear() {
        this.size = 0;
        this.records = new Record[30];
    }
}
