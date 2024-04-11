package com.example.tma_warehouse.models.user;

import com.example.tma_warehouse.models.administrator.Administrator;
import com.example.tma_warehouse.models.basic.BasicEntity;
import com.example.tma_warehouse.models.coordinator.Coordinator;
import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.user.enums.Status;
import com.example.tma_warehouse.models.user.role.Role;
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
import java.util.*;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();



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

    public User(String firstName,
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
        for (Role role : this.roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
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

