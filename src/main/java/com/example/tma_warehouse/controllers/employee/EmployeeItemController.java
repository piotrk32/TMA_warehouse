package com.example.tma_warehouse.controllers.employee;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee/items")
@RequiredArgsConstructor
@Tag(name = "Employee  Item Controller", description = "Controller for item management by employee")
public class EmployeeItemController {


}
