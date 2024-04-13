package com.example.tma_warehouse.models.request;

import com.example.tma_warehouse.models.basic.BasicEntity;
import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.item.enums.UnitOfMeasurement;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "requests")
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request extends BasicEntity {


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "employee_id", referencedColumnName = "id")
//    private Employee employee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @Column(name = "request_row_id", updatable = false, nullable = false)
    private Long requestRowId;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measurement")
    private UnitOfMeasurement unitOfMeasurement; // Zakładając, że UnitOfMeasurement to Enum

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "price_without_vat")
    private BigDecimal priceWithoutVAT;

    @Column(name = "comment")
    private String comment;



}
