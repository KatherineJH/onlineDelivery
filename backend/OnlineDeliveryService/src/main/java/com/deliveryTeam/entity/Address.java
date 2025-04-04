package com.deliveryTeam.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(nullable = false)
    private String streetName;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String zipCode;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    // PrePersist와 PreUpdate로 유효성 검사
    @PrePersist
    @PreUpdate
    public void validateAddress() {
        if (addressType == AddressType.USER) {
            if (user == null) {
                throw new IllegalArgumentException("Address must belong to a user.");
            }
            store = null; // store null로 설정
        } else if (addressType == AddressType.STORE) {
            if (store == null) {
                throw new IllegalArgumentException("Address must belong to a store.");
            }
            user = null; // user null로 설정
        } else {
            throw new IllegalArgumentException("Address must belong to either a user or a store.");
        }
    }

    public enum AddressType {
        USER,
        STORE
    }
}
