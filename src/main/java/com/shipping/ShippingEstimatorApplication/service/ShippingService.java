package com.shipping.ShippingEstimatorApplication.service;


import com.shipping.ShippingEstimatorApplication.dto.NearestWarehouseResponse;
import com.shipping.ShippingEstimatorApplication.dto.ShippingChargeRequest;
import com.shipping.ShippingEstimatorApplication.dto.ShippingChargeResponse;
import com.shipping.ShippingEstimatorApplication.entity.Customer;
import com.shipping.ShippingEstimatorApplication.entity.Product;
import com.shipping.ShippingEstimatorApplication.entity.Warehouse;
import com.shipping.ShippingEstimatorApplication.exception.ResourceNotFoundException;
import com.shipping.ShippingEstimatorApplication.repository.CustomerRepository;
import com.shipping.ShippingEstimatorApplication.repository.ProductRepository;
import com.shipping.ShippingEstimatorApplication.util.DeliverySpeedUtil;
import com.shipping.ShippingEstimatorApplication.util.HaversineUtil;
import com.shipping.ShippingEstimatorApplication.util.TransportModeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseService warehouseService;  // reuse warehouse logic



    public ShippingChargeResponse calculateChargeFromWarehouse(
            Long warehouseId,
            Long customerId,
            Long productId,
            String deliverySpeed) {


        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + customerId
                ));


        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId
                ));


        Warehouse warehouse = warehouseService
                .findNearestWarehouseEntity(product.getSeller().getId());


        double distanceKm = HaversineUtil.calculateDistance(
                warehouse.getLatitude(),  warehouse.getLongitude(),
                customer.getLatitude(),   customer.getLongitude()
        );


        double baseCharge = TransportModeUtil
                .calculateBaseShippingCharge(distanceKm, product.getWeightKg());


        double finalCharge = DeliverySpeedUtil
                .calculateFinalCharge(deliverySpeed, baseCharge, product.getWeightKg());


        return ShippingChargeResponse.builder()
                .shippingCharge(Math.round(finalCharge * 100.0) / 100.0)
                .transportMode(TransportModeUtil.getTransportMode(distanceKm))
                .distanceKm(Math.round(distanceKm * 100.0) / 100.0)
                .deliverySpeed(deliverySpeed.toLowerCase())
                .nearestWarehouse(NearestWarehouseResponse.builder()
                        .warehouseId(warehouse.getId())
                        .warehouseName(warehouse.getName())
                        .latitude(warehouse.getLatitude())
                        .longitude(warehouse.getLongitude())
                        .city(warehouse.getCity())
                        .state(warehouse.getState())
                        .build())
                .build();
    }


    public ShippingChargeResponse calculateFullShippingCharge(
            ShippingChargeRequest request) {


        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + request.getCustomerId()
                ));


        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + request.getProductId()
                ));


        Warehouse warehouse = warehouseService
                .findNearestWarehouseEntity(request.getSellerId());


        double distanceKm = HaversineUtil.calculateDistance(
                warehouse.getLatitude(),  warehouse.getLongitude(),
                customer.getLatitude(),   customer.getLongitude()
        );


        double baseCharge = TransportModeUtil
                .calculateBaseShippingCharge(distanceKm, product.getWeightKg());

        // Step 6: Final charge with delivery speed
        double finalCharge = DeliverySpeedUtil
                .calculateFinalCharge(
                        request.getDeliverySpeed(),
                        baseCharge,
                        product.getWeightKg()
                );


        return ShippingChargeResponse.builder()
                .shippingCharge(Math.round(finalCharge * 100.0) / 100.0)
                .transportMode(TransportModeUtil.getTransportMode(distanceKm))
                .distanceKm(Math.round(distanceKm * 100.0) / 100.0)
                .deliverySpeed(request.getDeliverySpeed().toLowerCase())
                .nearestWarehouse(NearestWarehouseResponse.builder()
                        .warehouseId(warehouse.getId())
                        .warehouseName(warehouse.getName())
                        .latitude(warehouse.getLatitude())
                        .longitude(warehouse.getLongitude())
                        .city(warehouse.getCity())
                        .state(warehouse.getState())
                        .build())
                .build();
    }
}