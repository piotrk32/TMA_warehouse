package com.example.tma_warehouse.models.coordinator;

import com.example.tma_warehouse.models.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "coordinators")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coordinator extends User {


    public Coordinator(
                    String firstName,
                    String lastName,
                    LocalDate birthDate,
                    String email,
                    String phoneNumber,
                    String accessToken,
                    String refreshToken,
                    String idToken) {
        super(firstName, lastName, birthDate, email, phoneNumber, accessToken, refreshToken, idToken);
    }

}