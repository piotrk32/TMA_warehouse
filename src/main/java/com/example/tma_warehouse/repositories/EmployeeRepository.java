package com.example.tma_warehouse.repositories;

import com.example.tma_warehouse.models.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
