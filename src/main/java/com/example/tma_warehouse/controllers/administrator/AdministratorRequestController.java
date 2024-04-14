package com.example.tma_warehouse.controllers.administrator;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee/items")
@Tag(name = "Administrator Request Controller", description = "Functionalities intended for administrators to manage requests")
public class AdministratorRequestController {
}
