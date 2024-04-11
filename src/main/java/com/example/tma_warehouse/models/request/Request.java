package com.example.tma_warehouse.models.request;

import com.example.tma_warehouse.models.basic.BasicEntity;
import com.example.tma_warehouse.models.employee.Employee;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "employee_first_name", referencedColumnName = "firstName"),
            @JoinColumn(name = "employee_last_name", referencedColumnName = "lastName")
    })
    private Employee employee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    Item item;

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_row_id_generator")
    @SequenceGenerator(name = "request_row_id_generator", sequenceName = "request_row_id_seq", allocationSize = 1)
    @Column(name = "request_row_id", updatable = false, nullable = false)
    private Long requestRowId;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measurement")
    private UnitOfMeasurement unitOfMeasurement; // Założenie, że UnitOfMeasurement to Enum

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "price_without_vat")
    private BigDecimal priceWithoutVAT;

    @Column(name = "comment")
    private String comment;



}
