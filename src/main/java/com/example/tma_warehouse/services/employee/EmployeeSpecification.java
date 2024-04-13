package com.example.tma_warehouse.services.employee;


import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.user.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeSpecification {

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Specification<Employee> firstNameContains(String firstName) {
        return (root, query, cb) -> firstName == null ? null :
                cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<Employee> lastNameContains(String lastName) {
        return (root, query, cb) -> lastName == null ? null :
                cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<Employee> emailContains(String email) {
        return (root, query, cb) -> email == null ? null :
                cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<Employee> phoneNumberContains(String phoneNumber) {
        return (root, query, cb) -> phoneNumber == null ? null :
                cb.like(cb.lower(root.get("phoneNumber")), "%" + phoneNumber.toLowerCase() + "%");
    }

    public static Specification<Employee> birthDateEquals(String birthDate) {
        return (root, query, cb) -> cb.equal(root.get("birthDate"), LocalDate.parse(birthDate, dateTimeFormatter));
    }

    public static Specification<Employee> statusEquals(String status) {
        return (root, query, cb) -> {
            Status enumStatus = Status.valueOf(status);
            return cb.equal(root.get("status"), enumStatus);
        };
    }

}
