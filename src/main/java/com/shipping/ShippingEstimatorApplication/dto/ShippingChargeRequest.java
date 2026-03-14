package com.shipping.ShippingEstimatorApplication.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ShippingChargeRequest {

    @NotNull(message = "sellerId is required")
    private Long sellerId;

    @NotNull(message = "customerId is required")
    private Long customerId;

    @NotNull(message = "productId is required")
    private Long productId;                    

    @NotNull(message = "deliverySpeed is required")
    @Pattern(
            regexp = "standard|express",
            flags  = Pattern.Flag.CASE_INSENSITIVE,
            message = "deliverySpeed must be 'standard' or 'express'"
    )
    private String deliverySpeed;
}

