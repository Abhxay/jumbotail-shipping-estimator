package com.shipping.ShippingEstimatorApplication.service;


import com.shipping.ShippingEstimatorApplication.dto.NearestWarehouseResponse;
import com.shipping.ShippingEstimatorApplication.entity.Seller;
import com.shipping.ShippingEstimatorApplication.entity.Warehouse;
import com.shipping.ShippingEstimatorApplication.exception.ResourceNotFoundException;
import com.shipping.ShippingEstimatorApplication.repository.SellerRepository;
import com.shipping.ShippingEstimatorApplication.repository.WarehouseRepository;
import com.shipping.ShippingEstimatorApplication.util.HaversineUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {


    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SellerRepository sellerRepository;


    public NearestWarehouseResponse findNearestWarehouse(Long sellerId) {


        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Seller not found with id: " + sellerId
                ));


        List<Warehouse> activeWarehouses = warehouseRepository.findByActiveTrue();

        if (activeWarehouses.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No active warehouses available at the moment"
            );
        }

        Warehouse nearest = findNearest(seller, activeWarehouses);


        return NearestWarehouseResponse.builder()
                .warehouseId(nearest.getId())
                .warehouseName(nearest.getName())
                .latitude(nearest.getLatitude())
                .longitude(nearest.getLongitude())
                .city(nearest.getCity())
                .state(nearest.getState())
                .build();
    }


    private Warehouse findNearest(Seller seller, List<Warehouse> warehouses) {

        Warehouse nearest        = null;
        double    shortestDistKm = Double.MAX_VALUE;  // start with infinity

        for (Warehouse warehouse : warehouses) {
            double distanceKm = HaversineUtil.calculateDistance(
                    seller.getLatitude(),    seller.getLongitude(),
                    warehouse.getLatitude(), warehouse.getLongitude()
            );

            System.out.println("Distance from seller '" + seller.getName()
                    + "' to '" + warehouse.getName()
                    + "': " + String.format("%.2f", distanceKm) + " km");

            if (distanceKm < shortestDistKm) {
                shortestDistKm = distanceKm;
                nearest        = warehouse;
            }
        }

        System.out.println("Nearest warehouse selected: " + nearest.getName());
        return nearest;
    }

    public Warehouse findNearestWarehouseEntity(Long sellerId) {

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Seller not found with id: " + sellerId
                ));

        List<Warehouse> activeWarehouses = warehouseRepository.findByActiveTrue();

        if (activeWarehouses.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No active warehouses available"
            );
        }

        return findNearest(seller, activeWarehouses);
    }
}