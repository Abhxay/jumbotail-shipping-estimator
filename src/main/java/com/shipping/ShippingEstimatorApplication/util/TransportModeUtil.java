package com.shipping.ShippingEstimatorApplication.util;

public class TransportModeUtil {


    private static final double AEROPLANE_RATE = 1.0;
    private static final double TRUCK_RATE      = 2.0;
    private static final double MINI_VAN_RATE   = 3.0;


    private static final double AEROPLANE_MIN_KM = 500.0;
    private static final double TRUCK_MIN_KM     = 100.0;


    public static double calculateBaseShippingCharge(double distanceKm,
                                                     double weightKg) {
        double rate;
        String mode;

        if (distanceKm >= AEROPLANE_MIN_KM) {
            rate = AEROPLANE_RATE;
            mode = "AEROPLANE";
        } else if (distanceKm >= TRUCK_MIN_KM) {
            rate = TRUCK_RATE;
            mode = "TRUCK";
        } else {
            rate = MINI_VAN_RATE;
            mode = "MINI_VAN";
        }

        System.out.println("Transport Mode Selected: " + mode
                + " | Distance: " + distanceKm + " km"
                + " | Weight: " + weightKg + " kg"
                + " | Rate: Rs" + rate + "/km/kg");

        // Formula: distance × weight × rate
        return distanceKm * weightKg * rate;
    }

    public static String getTransportMode(double distanceKm) {
        if (distanceKm >= AEROPLANE_MIN_KM) return "AEROPLANE";
        if (distanceKm >= TRUCK_MIN_KM)     return "TRUCK";
        return "MINI_VAN";
    }
}