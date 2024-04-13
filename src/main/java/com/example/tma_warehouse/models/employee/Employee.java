package com.example.tma_warehouse.models.employee;


import com.example.tma_warehouse.models.request.Request;
import com.example.tma_warehouse.models.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee extends User {

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Request> requests;

    public Employee(
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
