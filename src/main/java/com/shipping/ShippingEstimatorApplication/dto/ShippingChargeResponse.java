package com.shipping.ShippingEstimatorApplication.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingChargeResponse {

    private Double  shippingCharge;
    private String  transportMode;
    private Double  distanceKm;
    private String  deliverySpeed;

    private NearestWarehouseResponse nearestWarehouse;
}