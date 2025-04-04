package com.deliveryTeam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String rating; // Positive or Negative

    private double percentage; // 100.0 is the best, 0.0 is the worst

    // single review can have only one restaurant, however, one restaurant can have multiple reviews
    @ManyToOne
    private Store store;

    // single review can have only one user, however, one user can have multiple reviews
    @ManyToOne
    private User user;
}

