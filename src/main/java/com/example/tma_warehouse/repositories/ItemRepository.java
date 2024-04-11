package com.example.tma_warehouse.repositories;

import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
