package dental.app.records;

import dental.app.MyList;

import java.io.Serializable;
import java.util.Objects;

/**
 * The class represent the work type object and contains entries of type title, price and quantity of work items.
 */
public record Work (String title, byte quantity, int price) implements Serializable {

    /**
     * Count total amount of the work price.
     * @return The value of the amount
     */
    public int countAmount() {
        return quantity * price;
    }

    public enum SearchBy implements MyList.Searchable {
        title
    }

        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Work work = (Work) o;
        return quantity == work.quantity && price == work.price && Objects.equals(title, work.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, quantity, price);
    }

    @Override
    public String toString() {
        return "Work{" +
                "title='" + title + '\'' +
                ", quantity=" + quantity +
                ", price=" + countAmount() +
                '}';
    }
}
