package com.example.tma_warehouse.controllers.common;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common/item")
@RequiredArgsConstructor
@Tag(name = "Common Item Controller", description = "Controller for common item management")
public class ItemController {
}
