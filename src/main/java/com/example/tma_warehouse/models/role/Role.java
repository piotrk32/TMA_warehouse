package com.example.tma_warehouse.models.role;

import com.example.tma_warehouse.models.basic.BasicEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Role extends BasicEntity {

    private String name;

    // Konstruktory, gettery i settery
}
