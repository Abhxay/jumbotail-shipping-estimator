package com.shipping.ShippingEstimatorApplication.service;



import com.shipping.ShippingEstimatorApplication.dto.NearestWarehouseResponse;
import com.shipping.ShippingEstimatorApplication.entity.Seller;
import com.shipping.ShippingEstimatorApplication.entity.Warehouse;
import com.shipping.ShippingEstimatorApplication.exception.ResourceNotFoundException;
import com.shipping.ShippingEstimatorApplication.repository.SellerRepository;
import com.shipping.ShippingEstimatorApplication.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)   // Tells JUnit to use Mockito
class WarehouseServiceTest {

    // @Mock creates a FAKE version of these — no real DB calls
    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private SellerRepository sellerRepository;

    // @InjectMocks creates the REAL service but injects the fakes above
    @InjectMocks
    private WarehouseService warehouseService;

    // Test data we reuse across tests
    private Seller  testSeller;
    private Warehouse nearWarehouse;
    private Warehouse farWarehouse;

    @BeforeEach   // Runs before EVERY test method
    void setUp() {

        // Seller located in Chennai
        testSeller = Seller.builder()
                .id(1L)
                .name("Nestle Seller")
                .latitude(13.08268)
                .longitude(80.27045)
                .city("Chennai")
                .state("Tamil Nadu")
                .active(true)
                .build();

        // Warehouse close to seller
        nearWarehouse = Warehouse.builder()
                .id(1L)
                .name("BLR_Warehouse")
                .latitude(12.99999)
                .longitude(77.92327)
                .city("Bangalore")
                .state("Karnataka")
                .active(true)
                .build();

        // Warehouse far from seller
        farWarehouse = Warehouse.builder()
                .id(2L)
                .name("MUMB_Warehouse")
                .latitude(19.07283)
                .longitude(72.88261)
                .city("Mumbai")
                .state("Maharashtra")
                .active(true)
                .build();
    }

    // ── TEST 1: Happy path ─────────────────────────────────────────
    @Test
    void findNearestWarehouse_ShouldReturnClosestWarehouse() {

        // ARRANGE — tell the fakes what to return
        when(sellerRepository.findById(1L))
                .thenReturn(Optional.of(testSeller));
        when(warehouseRepository.findByActiveTrue())
                .thenReturn(List.of(nearWarehouse, farWarehouse));

        // ACT — call the real method
        NearestWarehouseResponse response =
                warehouseService.findNearestWarehouse(1L);

        // ASSERT — verify the result is correct
        assertNotNull(response);
        assertEquals("BLR_Warehouse", response.getWarehouseName());
        assertEquals(1L, response.getWarehouseId());
    }

    // ── TEST 2: Seller not found ───────────────────────────────────
    @Test
    void findNearestWarehouse_WhenSellerNotFound_ShouldThrow404() {

        // ARRANGE — simulate seller missing from DB
        when(sellerRepository.findById(999L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT — expect exception to be thrown
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> warehouseService.findNearestWarehouse(999L)
        );

        assertEquals("Seller not found with id: 999", exception.getMessage());
    }

    // ── TEST 3: No active warehouses ──────────────────────────────
    @Test
    void findNearestWarehouse_WhenNoWarehouses_ShouldThrow404() {

        when(sellerRepository.findById(1L))
                .thenReturn(Optional.of(testSeller));
        when(warehouseRepository.findByActiveTrue())
                .thenReturn(Collections.emptyList());  // empty list

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> warehouseService.findNearestWarehouse(1L)
        );

        assertTrue(exception.getMessage()
                .contains("No active warehouses"));
    }

    // ── TEST 4: Only one warehouse available ──────────────────────
    @Test
    void findNearestWarehouse_WithSingleWarehouse_ShouldReturnIt() {

        when(sellerRepository.findById(1L))
                .thenReturn(Optional.of(testSeller));
        when(warehouseRepository.findByActiveTrue())
                .thenReturn(List.of(nearWarehouse));   // only one

        NearestWarehouseResponse response =
                warehouseService.findNearestWarehouse(1L);

        assertEquals("BLR_Warehouse", response.getWarehouseName());
    }
}