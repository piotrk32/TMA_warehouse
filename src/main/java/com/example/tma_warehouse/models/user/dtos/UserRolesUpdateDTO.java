package com.example.tma_warehouse.models.user.dtos;

import java.util.List;

public class UserRolesUpdateDTO {
    private List<String> roles;

    public UserRolesUpdateDTO() {}

    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
