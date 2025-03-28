package com.deliveryTeam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    private Long categoryId;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();
}