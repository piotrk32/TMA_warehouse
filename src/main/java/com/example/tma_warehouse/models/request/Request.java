package com.example.tma_warehouse.models.request;

import com.example.tma_warehouse.models.basic.BasicEntity;
import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.item.enums.UnitOfMeasurement;
import com.example.tma_warehouse.models.request.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;


@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    Item item; // Relation to Item entity already exists

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measurement")
    UnitOfMeasurement unitOfMeasurement; // Assuming UnitOfMeasurement is an Enum

    @Column(name = "quantity")
    BigDecimal quantity;

    @Column(name = "price_without_vat")
    BigDecimal priceWithoutVAT;

    @Column(name = "comment", columnDefinition = "TEXT")
    String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    RequestStatus status; // Assuming RequestStatus is an Enum representing the status

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_row_id_gen")
    @SequenceGenerator(name = "request_row_id_gen", sequenceName = "request_row_id_seq", allocationSize = 1)
    @Column(name = "request_row_id", updatable = false, nullable = false)
    private Long requestRowId;

    // Constructor, including all fields except 'requestId' which is auto-generated
    public Request(Employee employee, Item item, UnitOfMeasurement unitOfMeasurement,
                   BigDecimal quantity, BigDecimal priceWithoutVAT, String comment,
                   RequestStatus status) {
        this.employee = employee;
        this.item = item;
        this.unitOfMeasurement = unitOfMeasurement;
        this.quantity = quantity;
        this.priceWithoutVAT = priceWithoutVAT;
        this.comment = comment;
        this.status = status;
    }
}
