package com.deliveryTeam.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;

    @Min(value = 0, message = "Price must be non-negative")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne private Store store;
}
