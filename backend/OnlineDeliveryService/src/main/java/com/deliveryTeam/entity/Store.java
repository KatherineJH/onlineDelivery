package com.deliveryTeam.entity;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
public class Store {

    @Id @GeneratedValue
    private Long storeId;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "store")
    private List<Product> products = new ArrayList<>();
}