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


}
