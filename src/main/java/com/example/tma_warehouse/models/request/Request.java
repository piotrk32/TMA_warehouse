package com.example.tma_warehouse.models.request;

import com.example.tma_warehouse.models.RowRequest.RowRequest;
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
import java.util.ArrayList;
import java.util.List;


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


    @Column(name = "price_without_vat", nullable = true)
    BigDecimal priceWithoutVAT;

    @Column(name = "comment", columnDefinition = "TEXT")
    String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    RequestStatus status; // Assuming RequestStatus is an Enum representing the status

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RowRequest> rowRequests = new ArrayList<>();


    // Constructor, including all fields except 'requestId' which is auto-generated
    public Request(Employee employee, Item item,
                   BigDecimal quantity, BigDecimal priceWithoutVAT, String comment,
                   RequestStatus status) {
        this.employee = employee;
        this.priceWithoutVAT = priceWithoutVAT;
        this.comment = comment;
        this.status = status;
    }
}
