package com.example.tma_warehouse.services.rowrequest;

import com.example.tma_warehouse.models.RowRequest.RowRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

;


public class RowRequestSpecification {

    public static Specification<RowRequest> hasRequestId(Long requestId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("request").get("id"), requestId);
    }

    public static Specification<RowRequest> hasItemId(Long itemId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("item").get("id"), itemId);
    }

    public static Specification<RowRequest> createdBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate != null && toDate != null) {
                return criteriaBuilder.between(root.get("createdAt"), fromDate, toDate);
            }
            return criteriaBuilder.conjunction();
        };
    }



    public static Specification<RowRequest> quantityBetween(BigDecimal quantityFrom, BigDecimal quantityTo) {
        return (root, query, criteriaBuilder) -> {
            if (quantityFrom != null && quantityTo != null) {
                return criteriaBuilder.between(root.get("quantity"), quantityFrom, quantityTo);
            } else if (quantityFrom != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), quantityFrom);
            } else if (quantityTo != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), quantityTo);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static Specification<RowRequest> priceWithoutVatBetween(BigDecimal priceFrom, BigDecimal priceTo) {
        return (root, query, criteriaBuilder) -> {
            if (priceFrom != null && priceTo != null) {
                return criteriaBuilder.between(root.get("priceWithoutVAT"), priceFrom, priceTo);
            } else if (priceFrom != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("priceWithoutVAT"), priceFrom);
            } else if (priceTo != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("priceWithoutVAT"), priceTo);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static Specification<RowRequest> createdBetween(String fromDateStr, String toDateStr) {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime fromDate = null;
            LocalDateTime toDate = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            if (fromDateStr != null && !fromDateStr.isEmpty()) {
                fromDate = LocalDateTime.parse(fromDateStr + "T00:00:00", formatter);
            }
            if (toDateStr != null && !toDateStr.isEmpty()) {
                toDate = LocalDateTime.parse(toDateStr + "T23:59:59", formatter);
            }

            if (fromDate != null && toDate != null) {
                return criteriaBuilder.between(root.get("createdAt"), fromDate, toDate);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }



}

