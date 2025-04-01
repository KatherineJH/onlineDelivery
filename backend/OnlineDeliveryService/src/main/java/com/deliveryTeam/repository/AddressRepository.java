package com.deliveryTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliveryTeam.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {}
