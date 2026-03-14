package com.shipping.ShippingEstimatorApplication.service;


import com.shipping.ShippingEstimatorApplication.dto.NearestWarehouseResponse;
import com.shipping.ShippingEstimatorApplication.entity.Seller;
import com.shipping.ShippingEstimatorApplication.entity.Warehouse;
import com.shipping.ShippingEstimatorApplication.exception.ResourceNotFoundException;
import com.shipping.ShippingEstimatorApplication.repository.SellerRepository;
import com.shipping.ShippingEstimatorApplication.repository.WarehouseRepository;
import com.shipping.ShippingEstimatorApplication.util.HaversineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Service
public class WarehouseService {

    private static final Logger log = LoggerFactory.getLogger(WarehouseService.class);

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Cacheable(value = "nearestWarehouse", key = "#sellerId")
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

            log.info("Distance from seller '{}' to '{}': {} km",
                    seller.getName(),
                    warehouse.getName(),
                    String.format("%.2f", distanceKm));


            if (distanceKm < shortestDistKm) {
                shortestDistKm = distanceKm;
                nearest        = warehouse;
            }
        }

        log.info("Nearest warehouse selected: {}", nearest.getName());
        return nearest;
    }

    @Cacheable(value = "nearestWarehouseEntity", key = "#sellerId")
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