package com.example.tma_warehouse.models.administrator;

import com.example.tma_warehouse.models.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Administrator extends User {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(super.getAuthorities());
        // Dodanie dodatkowych uprawnień dla administratora
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));
        // Możesz dodać więcej uprawnień specyficznych dla roli Administratora
        return authorities;
    }
}
