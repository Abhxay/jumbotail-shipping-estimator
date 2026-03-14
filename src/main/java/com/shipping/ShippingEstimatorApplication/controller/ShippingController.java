package com.shipping.ShippingEstimatorApplication.controller;


import com.shipping.ShippingEstimatorApplication.dto.ShippingChargeRequest;
import com.shipping.ShippingEstimatorApplication.dto.ShippingChargeResponse;
import com.shipping.ShippingEstimatorApplication.service.ShippingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shipping-charge")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;


    @GetMapping
    public ResponseEntity<ShippingChargeResponse> getShippingCharge(
            @RequestParam Long warehouseId,
            @RequestParam Long customerId,
            @RequestParam Long productId,
            @RequestParam String deliverySpeed) {

        ShippingChargeResponse response = shippingService
                .calculateChargeFromWarehouse(
                        warehouseId,
                        customerId,
                        productId,
                        deliverySpeed
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate")
    public ResponseEntity<ShippingChargeResponse> calculateShippingCharge(
            @Valid @RequestBody ShippingChargeRequest request) {
        //  ↑           ↑
        // @Valid        triggers @NotNull/@Pattern validation on the request
        // @RequestBody  reads JSON body and maps it to ShippingChargeRequest

        ShippingChargeResponse response =
                shippingService.calculateFullShippingCharge(request);

        return ResponseEntity.ok(response);
    }
}
