package com.example.tma_warehouse.models.item;

import com.example.tma_warehouse.models.RowRequest.RowRequest;
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
import java.util.List;

@Entity
@Table(name = "items")
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item extends BasicEntity {

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_group")
    @Enumerated(EnumType.STRING)
    private ItemGroup itemGroup;

    @Column(name = "unit_of_measurement")
    @Enumerated(EnumType.STRING)
    private UnitOfMeasurement unitOfMeasurement;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "price_without_vat")
    private BigDecimal priceWithoutVat;

    @Column(name = "status")
    private String status;

    @Column(name = "storage_location")
    private String storageLocation;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "photo")
    private String photoPath;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RowRequest> rowRequests;

    public Item(String itemName, ItemGroup itemGroup, UnitOfMeasurement unitOfMeasurement, BigDecimal quantity, BigDecimal priceWithoutVat, String status, String storageLocation, String contactPerson, String photoPath) {
        this.itemName = itemName;
        this.itemGroup = itemGroup;
        this.unitOfMeasurement = unitOfMeasurement;
        this.quantity = quantity;
        this.priceWithoutVat = priceWithoutVat;
        this.status = status;
        this.storageLocation = storageLocation;
        this.contactPerson = contactPerson;
        this.photoPath = photoPath;
    }

}
