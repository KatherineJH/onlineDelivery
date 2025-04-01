package com.deliveryTeam.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String name;

    //    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)  //
    // Category has multiple products
    //    private List<Product> products = new ArrayList<>();

    //    @JsonIgnore
    //    @ManyToOne
    //    private Store store;
}
