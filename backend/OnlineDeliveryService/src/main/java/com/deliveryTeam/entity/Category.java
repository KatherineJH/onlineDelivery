package com.deliveryTeam.entity;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
public class Category {

    @Id @GeneratedValue
    private Long categoryId;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();
}
