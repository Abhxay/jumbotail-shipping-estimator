package com.shipping.ShippingEstimatorApplication.util;

public class HaversineUtil {

    // Earth's radius
    private static final double EARTH_RADIUS_KM = 6371.0;


    public static double calculateDistance(double lat1, double lon1,
                                           double lat2, double lon2) {

        //Convert degrees → radians
        // Math functions in Java work in radians, not degrees
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);


        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        // Haversine Formula
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Multiply by earth radius to get KM
        return EARTH_RADIUS_KM * c;
    }
}