package com.shipping.ShippingEstimatorApplication.controller;



import com.shipping.ShippingEstimatorApplication.dto.NearestWarehouseResponse;
import com.shipping.ShippingEstimatorApplication.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;


    @GetMapping("/nearest")
    public ResponseEntity<NearestWarehouseResponse> getNearestWarehouse(
            @RequestParam Long sellerId,
            @RequestParam Long productId) {


        NearestWarehouseResponse response =
                warehouseService.findNearestWarehouse(sellerId);

        return ResponseEntity.ok(response);
    }
}
