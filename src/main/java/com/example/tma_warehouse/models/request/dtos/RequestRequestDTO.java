package com.example.tma_warehouse.models.request.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Data Transfer Object for row request filtering and pagination")
public class RequestRequestDTO {

    @Schema(example = "0")
    @Min(value = 0, message = "Page index must not be less than zero.")
    private String page = "0";

    @Schema(example = "10")
    @Min(value = 1, message = "Page size must be at least one.")
    private String size = "10";

    @Schema(example = "item")
    private String sortParam = "status";

    @Schema(example = "ASC")
    private String direction = "ASC";

    @Schema(example = "Car")
    @Length(min = 3, message = "Item name search must be at least 3 characters long.")
    private String employeeNameSearch = null;

    @Schema(example = "2021-10-01")
    private String fromDate = null; // Assuming filter by the date range

    @Schema(example = "2021-12-01")
    private String toDate = null;

    @Schema(example = "200")
    @Min(value = 0, message = "Minimum quantity must not be less than zero.")
    private BigDecimal priceWithoutVatFrom = null;

    @Schema(example = "500")
    @Min(value = 0, message = "Maximum quantity must not be less than zero.")
    private BigDecimal priceWithoutVatTo = null;

    @Schema(example = "New")
    @Length(min = 3, message = "Status search must be at least 3 characters long.")
    private String status = null; // Assuming you want to filter by status if applicable

    @Schema(example = "Comment here")
    @Length(min = 3, message = "Comment must be at least 3 characters long.")
    private String comment = null; // Assuming you want to filter by comments in the row request

}
