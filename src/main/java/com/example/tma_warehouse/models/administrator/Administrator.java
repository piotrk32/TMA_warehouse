package com.example.tma_warehouse.models.administrator;

import com.example.tma_warehouse.models.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//@Entity
//@Table(name = "administrators")
//@Getter
//@Setter
//@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)

public class Administrator extends User {

    public Administrator(
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

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>(super.getAuthorities());
//        // Dodanie dodatkowych uprawnień dla administratora
//        authorities.add(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));
//        // Możesz dodać więcej uprawnień specyficznych dla roli Administratora
//        return authorities;
//    }
}
