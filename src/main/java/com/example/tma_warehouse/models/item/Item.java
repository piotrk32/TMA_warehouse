package com.example.tma_warehouse.models.item;

import com.example.tma_warehouse.models.basic.BasicEntity;
import com.example.tma_warehouse.models.item.enums.ItemGroup;
import com.example.tma_warehouse.models.item.enums.UnitOfMeasurement;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "items")
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item extends BasicEntity {


    @Column(name = "item_group")
    @Enumerated(EnumType.STRING)
    private ItemGroup itemGroup;

    @Column(name = "unit_of_measurement")
    @Enumerated(EnumType.STRING)
    private UnitOfMeasurement unitOfMeasurement;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "price_without_vat")
    private BigDecimal priceWithoutVAT;

    @Column(name = "status")
    private String status;

    @Column(name = "storage_location")
    private String storageLocation;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "photo")
    private String photoPath; // może przechowywać ścieżkę do zdjęcia lub UR


}
