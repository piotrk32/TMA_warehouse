package com.example.tma_warehouse.models.role;

import com.example.tma_warehouse.models.basic.BasicEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role extends BasicEntity {

    private String name;

    // Konstruktory, gettery i settery
}
