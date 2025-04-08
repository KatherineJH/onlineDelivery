package com.deliveryTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliveryTeam.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {}
