package com.example.tma_warehouse.models.user;


import com.example.tma_warehouse.models.administrator.Administrator;
import com.example.tma_warehouse.models.basic.BasicEntity;
import com.example.tma_warehouse.models.coordinator.Coordinator;
import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.user.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BasicEntity implements UserDetails {


    String firstName;
    String lastName;
    LocalDate birthDate;
    String email;
    String phoneNumber;
    String accessToken;
    String refreshToken;
    String idToken;

    @Enumerated(EnumType.STRING)
    Status status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

    public User(String email,
                String accessToken,
                String refreshToken,
                String idToken) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.idToken = idToken;
        this.status = Status.REGISTRATION_INCOMPLETE;
    }

    public User(
                String firstName,
                String lastName,
                LocalDate birthDate,
                String email,
                String phoneNumber,
                String accessToken,
                String refreshToken,
                String idToken) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.idToken = idToken;
        this.status = Status.ACTIVE;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (this instanceof Employee) {
            authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        } else if (this instanceof Coordinator) {
            authorities.add(new SimpleGrantedAuthority("ROLE_COORDINATOR"));
        } else if (this instanceof Administrator) {  // Check if the user is an Administrator
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));
        }


        if (this.status.equals(Status.REGISTRATION_INCOMPLETE)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_INCOMPLETE_REGISTRATION"));
        }


        if (this.roles != null) {
            this.roles.stream()
                    .map(role -> "ROLE_" + role.toUpperCase()) // Ensure roles are in the correct format
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authorities::add);
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == Status.ACTIVE || status == Status.REGISTRATION_INCOMPLETE;
    }

}
