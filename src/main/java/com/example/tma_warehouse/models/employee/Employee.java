package com.example.tma_warehouse.models.employee;

import com.example.tma_warehouse.models.role.Role;
import com.example.tma_warehouse.models.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "employes")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee extends User {



    public Employee(String firstName,
                    String lastName,
                    LocalDate birthDate,
                    String email,
                    String phoneNumber,
                    String accessToken,
                    String refreshToken,
                    String idToken,
                    Role role) {
        super(firstName, lastName, birthDate, email, phoneNumber, accessToken, refreshToken, idToken, role);
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

}
