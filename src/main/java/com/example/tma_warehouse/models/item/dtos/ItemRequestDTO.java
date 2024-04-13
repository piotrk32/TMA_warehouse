package com.example.tma_warehouse.models.item.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Data Transfer Object for item request")
public class ItemRequestDTO {

    @Schema(example = "0")
    @Min(value = 0, message = "Page index must not be less than zero.")
    private String page = "0";

    @Schema(example = "10")
    @Min(value = 1, message = "Page size must be at least one.")
    private String size = "10";

    @Schema(example = "itemGroup")
    private String sortParam = "itemGroup";

    @Schema(example = "ASC")
    private String direction = "ASC";

    @Schema(example = "Car")
    @Length(min = 3, message = "Item name search must be at least 3 characters long.")
    private String itemNameSearch = null;

    @Schema(example = "Electronics")
    @Length(min = 3, message = "Item group search must be at least 3 characters long.")
    private String itemGroupSearch = null;

    @Schema(example = "50")
    @Min(value = 0, message = "Minimum price must not be less than zero.")
    private String priceFrom = null;

    @Schema(example = "5000")
    @Min(value = 1, message = "Maximum price must be at least one.")
    private String priceTo = null;

    @Schema(example = "Active")
    @Length(min = 2, message = "Status search must be at least 2 characters long.")
    private String status = null;

    @Schema(example = "Main Warehouse")
    @Length(min = 3, message = "Storage location search must be at least 3 characters long.")
    private String storageLocation = null;


}
