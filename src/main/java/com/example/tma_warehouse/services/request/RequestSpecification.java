package com.example.tma_warehouse.services.request;

import com.example.tma_warehouse.models.request.Request;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestSpecification {

    public static Specification<Request> hasEmployeeName(String employeeName) {
        return (root, query, criteriaBuilder) -> {
            if (employeeName == null || employeeName.isBlank()) {
                return criteriaBuilder.conjunction(); // No condition
            }
            // Assuming 'employee' is the name of the attribute in Request entity that maps to Employee entity
            Expression<String> fullName = criteriaBuilder.concat(root.join("employee").get("firstName"),
                    criteriaBuilder.concat(" ", root.join("employee").get("lastName")));

            return criteriaBuilder.like(criteriaBuilder.lower(fullName),
                    "%" + employeeName.toLowerCase().trim() + "%");
        };
    }

    public static Specification<Request> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), status.toLowerCase().trim());
        };
    }

    public static Specification<Request> priceWithoutVatBetween(BigDecimal priceWithoutVatFrom, BigDecimal priceWithoutVatTo) {
        return (root, query, criteriaBuilder) -> {
            if (priceWithoutVatFrom != null && priceWithoutVatTo != null) {
                return criteriaBuilder.between(root.get("price_without_vat"), priceWithoutVatFrom, priceWithoutVatTo);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Request> hasCommentLike(String comment) {
        return (root, query, criteriaBuilder) -> {
            if (comment != null && !comment.isBlank()) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("comment")),
                        "%" + comment.toLowerCase().trim() + "%");
            }
            return criteriaBuilder.conjunction();
        };
    }

        public static Specification<Request> createdBetween(String fromDate, String toDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return (root, query, criteriaBuilder) -> {
            if (fromDate != null && toDate != null) {
                LocalDateTime fromDateTime = LocalDateTime.parse(fromDate, formatter);
                LocalDateTime toDateTime = LocalDateTime.parse(toDate, formatter);
                return criteriaBuilder.between(root.get("createdAt"), fromDateTime, toDateTime);
            }
            return criteriaBuilder.conjunction(); // No condition, effectively ignoring this filter
        };
    }
}
