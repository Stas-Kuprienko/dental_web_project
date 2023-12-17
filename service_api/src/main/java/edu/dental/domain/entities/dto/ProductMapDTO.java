package edu.dental.domain.entities.dto;

import edu.dental.domain.records.ProductMap;

import java.util.Arrays;
import java.util.Objects;

public class ProductMapDTO {

    private final ItemDTO[] items;

    public ProductMapDTO(ProductMap map) {
        this.items = new ItemDTO[map.size()];
        ProductMap.Item[] items1 = map.toArray();
        for (int i = 0; i < map.size(); i++) {
            items[i] = new ItemDTO(items1[i]);
        }
    }

    public String[] getKeys() {
        String[] result = new String[items.length];
        return Arrays.stream(items).map(e -> e.title).toList().toArray(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductMapDTO that = (ProductMapDTO) o;
        return Arrays.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(items);
    }

    @Override
    public String toString() {
        return "ProductMapDTO{" +
                "items=" + Arrays.toString(items) +
                '}';
    }

    public static class ItemDTO {

        private final int id;
        private final String title;
        private final int price;

        public ItemDTO(ProductMap.Item item) {
            this.id = item.getId();
            this.title = item.getKey();
            this.price = item.getValue();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ItemDTO itemDTO = (ItemDTO) o;
            return id == itemDTO.id && price == itemDTO.price && Objects.equals(title, itemDTO.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, title, price);
        }

        @Override
        public String toString() {
            return "ItemDTO{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", price=" + price +
                    '}';
        }
    }
}
