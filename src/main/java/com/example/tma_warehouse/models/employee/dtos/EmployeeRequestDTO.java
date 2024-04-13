package com.example.tma_warehouse.models.employee.dtos;

import com.example.tma_warehouse.validators.PageDirection;
import com.example.tma_warehouse.validators.PageSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema
@NoArgsConstructor
public class EmployeeRequestDTO {

    @Schema(example = "0")
    @Min(value = 0)
    private String page = "0";

    @Schema(example = "25")
    @PageSize
    private String size = "10";

    @Schema(example = "lastName")
    private String sortParam = "lastName";

    @Schema(example = "DESC")
    @PageDirection
    private String direction = "ASC";

    @Schema(example = "John")
    private String firstName = null;

    @Schema(example = "Doe")
    private String lastName = null;

    @Schema(example = "2000-01-01")
    private String birthDate = null;

    @Schema(example = "email@domain.com")
    private String email = null;

    @Schema(example = "ACTIVE")
    private String status = null;

    @Schema(example = "123123123")
    private String phoneNumber = null;

}
