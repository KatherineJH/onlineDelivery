package com.deliveryTeam.repository;

import com.deliveryTeam.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
