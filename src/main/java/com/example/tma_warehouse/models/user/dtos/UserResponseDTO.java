package com.example.tma_warehouse.models.user.dtos;

import com.example.tma_warehouse.models.user.enums.Status;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public record UserResponseDTO(Long userId,
                                  Status status,
                                  String firstName,
                                  String lastName,
                                  LocalDate birthDate,
                                  String email,
                                  String phoneNumber,
                                  List<String> roles) {

    @Builder
    public UserResponseDTO {}
}
