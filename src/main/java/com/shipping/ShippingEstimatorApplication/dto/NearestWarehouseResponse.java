package com.shipping.ShippingEstimatorApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearestWarehouseResponse {

    private Long   warehouseId;
    private String warehouseName;
    private Double latitude;
    private Double longitude;
    private String city;
    private String state;
}