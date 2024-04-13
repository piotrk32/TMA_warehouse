package com.example.tma_warehouse.services.item;


import com.example.tma_warehouse.models.item.Item;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecification {

    public static Specification<Item> itemCategoryContains(String category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("category")), "%" + category.toLowerCase() + "%");
        };
    }

    public static Specification<Item> priceGreaterThanOrEqual(Double priceFrom) {
        return (root, query, criteriaBuilder) -> {
            if (priceFrom == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("startingPrice"), priceFrom);
        };
    }

    public static Specification<Item> priceLessThanOrEqual(Double priceTo) {
        return (root, query, criteriaBuilder) -> {
            if (priceTo == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("startingPrice"), priceTo);
        };
    }
    public static Specification<Item> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isBlank()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // Jeśli status jest pusty, nie filtrujemy po nim
            }
            return criteriaBuilder.equal(root.get("status"), status.trim());
        };
    }

    public static Specification<Item> hasStorageLocation(String storageLocation) {
        return (root, query, criteriaBuilder) -> {
            if (storageLocation == null || storageLocation.isBlank()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // Jeśli lokalizacja magazynowa jest pusta, nie filtrujemy po niej
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("storageLocation")), "%" + storageLocation.toLowerCase().trim() + "%");
        };
    }


}
