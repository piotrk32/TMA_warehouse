package com.example.tma_warehouse.models.RowRequest;

import com.example.tma_warehouse.models.basic.BasicEntity;
import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.item.enums.UnitOfMeasurement;
import com.example.tma_warehouse.models.request.Request;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "rows_requests")
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RowRequest extends BasicEntity {

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;


    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measurement", nullable = false)
    UnitOfMeasurement unitOfMeasurement;

    @Column(name = "quantity")
    BigDecimal quantity;

    @Column(name = "price_without_vat")
    BigDecimal priceWithoutVat;

    @Column(name = "comment", columnDefinition = "TEXT")
    String comment;

    public RowRequest(Request request, UnitOfMeasurement unitOfMeasurement, BigDecimal quantity, BigDecimal priceWithoutVat, String comment) {
        this.request = request;
        this.unitOfMeasurement = unitOfMeasurement;
        this.quantity = quantity;
        this.priceWithoutVat = priceWithoutVat;
        this.comment = comment;
    }
}
