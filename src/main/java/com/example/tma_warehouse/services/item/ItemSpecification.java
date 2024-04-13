package com.example.tma_warehouse.services.item;


import com.example.tma_warehouse.models.item.Item;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ItemSpecification {

    public static Specification<Item> itemCategoryContains(String category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("itemGroup")), "%" + category.toLowerCase() + "%");
        };
    }

    public static Specification<Item> priceGreaterThanOrEqual(BigDecimal priceFrom) {
        return (root, query, criteriaBuilder) -> {
            if (priceFrom == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("priceWithoutVat"), priceFrom);
        };
    }

    public static Specification<Item> priceLessThanOrEqual(BigDecimal priceTo) {
        return (root, query, criteriaBuilder) -> {
            if (priceTo == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("priceWithoutVat"), priceTo);
        };
    }
    public static Specification<Item> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // No filtering condition
            }
            // Using lower case for both for case-insensitive comparison
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), status.toLowerCase().trim());
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

    public static Specification<Item> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("itemName")), "%" + name.toLowerCase() + "%");
        };
    }


}
