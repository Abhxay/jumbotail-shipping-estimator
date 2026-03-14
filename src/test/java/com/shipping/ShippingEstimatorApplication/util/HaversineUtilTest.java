package com.shipping.ShippingEstimatorApplication.util;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HaversineUtilTest {

    // ── TEST 1: Known distance between two real cities ─────────────
    @Test
    void calculateDistance_BangaloreToChennai_ShouldBeApprox290km() {

        double distance = HaversineUtil.calculateDistance(
                12.97194, 77.59369,   // Bangalore
                13.08268, 80.27045    // Chennai
        );

        // Real distance is ~290km — allow ±10km tolerance
        assertTrue(distance > 280 && distance < 300,
                "Expected ~290km but got: " + distance);
    }

    // ── TEST 2: Same point = zero distance ────────────────────────
    @Test
    void calculateDistance_SameCoordinates_ShouldReturnZero() {

        double distance = HaversineUtil.calculateDistance(
                12.99999, 77.92327,
                12.99999, 77.92327    // identical coords
        );

        assertEquals(0.0, distance, 0.001);  // allow tiny floating point error
    }

    // ── TEST 3: Transport mode selection ──────────────────────────
    @Test
    void getTransportMode_Above500km_ShouldReturnAeroplane() {
        assertEquals("AEROPLANE", TransportModeUtil.getTransportMode(600));
    }

    @Test
    void getTransportMode_Between100And500km_ShouldReturnTruck() {
        assertEquals("TRUCK", TransportModeUtil.getTransportMode(300));
    }

    @Test
    void getTransportMode_Below100km_ShouldReturnMiniVan() {
        assertEquals("MINI_VAN", TransportModeUtil.getTransportMode(50));
    }

    // ── TEST 4: Delivery speed calculation ────────────────────────
    @Test
    void calculateFinalCharge_Standard_ShouldAddFlatFee() {
        // base = 100, standard = 10 flat + 100 = 110
        double result = DeliverySpeedUtil
                .calculateFinalCharge("standard", 100.0, 5.0);
        assertEquals(110.0, result);
    }

    @Test
    void calculateFinalCharge_Express_ShouldAddFlatFeeAndPerKgCharge() {
        // base = 100, express = 10 + (1.2 * 5kg) + 100 = 116
        double result = DeliverySpeedUtil
                .calculateFinalCharge("express", 100.0, 5.0);
        assertEquals(116.0, result);
    }

    @Test
    void calculateFinalCharge_InvalidSpeed_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> DeliverySpeedUtil
                        .calculateFinalCharge("overnight", 100.0, 5.0));
    }
}