package com.deliveryTeam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String streetName;
    private String city;
    private String state;
    private String zipCode;

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

    // 사용자와 연결 (USER 타입일 때만 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 상점과 연결 (STORE 타입일 때만 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public enum AddressType {
        USER,
        STORE
    }
}
