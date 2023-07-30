package dental.app.records;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class of a type of work. Contains entries of type, price and number of items.
 */
public class Work implements Serializable {

    /**
     * The title of the work type.
     */
    private String title;

    /**
     * The quantity of the work items.
     */
    private byte quantity;

    /**
     * The price of the work.
     */
    private int price;

    /**
     * Create a new Work object
     * @param title   The title of type of the work
     * @param price  The price of the work
     * @param quantity The number of the work items
     */
    Work(String title, byte quantity, int price) {
        if (title == null || title.isEmpty()) {
            return;
        }
        this.title = title;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * Count total amount of the work price.
     * @return The value of the amount
     */
    public int countAmount() {
        return quantity * price;
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
                ", price=" + price +
                '}';
    }

    /*                           ||
            Getters and setters  \/
    */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public byte getNumber() {
        return quantity;
    }

    public void setNumber(byte quantity) {
        this.quantity = quantity;
    }

}
