package com.deliveryTeam.http.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItemRequest {
    private Long productId;
    private Integer quantity;
}
