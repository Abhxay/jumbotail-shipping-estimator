package com.shipping.ShippingEstimatorApplication.util;


public class DeliverySpeedUtil {

    private static final double STANDARD_COURIER_CHARGE = 10.0;
    private static final double EXPRESS_EXTRA_PER_KG    = 1.2;



    public static double calculateFinalCharge(String deliverySpeed,
                                              double baseCharge,
                                              double weightKg) {
        String speed = deliverySpeed.trim().toLowerCase();

        return switch (speed) {
            case "standard" -> STANDARD_COURIER_CHARGE + baseCharge;

            case "express"  -> STANDARD_COURIER_CHARGE
                    + (EXPRESS_EXTRA_PER_KG * weightKg)
                    + baseCharge;

            default -> throw new IllegalArgumentException(
                    "Invalid delivery speed: '" + deliverySpeed
                            + "'. Accepted values: 'standard', 'express'"
            );
        };
    }
}