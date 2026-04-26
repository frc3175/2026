package frc.robot.util;

import java.util.Map;
import java.util.TreeMap;

import frc.robot.Constants;

public class ShooterLookup {

    private static final TreeMap<Double, Double> velocityMap = new TreeMap<>();

    public ShooterLookup()
    {
        // example values:
        // key: distance to target in meters
        // value: shooter wheel velocity 
        velocityMap.put(1.93, Constants.ShooterConstants.BUMPTELESPEED);
        velocityMap.put(2.74, -46.0);
        velocityMap.put(2.87, Constants.ShooterConstants.TOWERSPINSPEED);
        velocityMap.put(3.5, Constants.ShooterConstants.TRENCHSPINSPEED);
        velocityMap.put(4.5, -53.0);
        velocityMap.put(5.3, -56.0);
    }

    public static double calculateFlywheelVelocity(double distanceMeters) {
        return performLookup(velocityMap, distanceMeters);
    }
    
    // helpers to perform interpolation in case of multiple tables:

    private static double performLookup(TreeMap<Double, Double> map, double distanceMeters) {

        if (map.isEmpty()) {
            return -1;
        }

        if (distanceMeters < map.firstKey()) {
            return map.get(map.firstKey());
        }
        if (distanceMeters > map.lastKey()) {
            return map.get(map.lastKey());
        }

        Map.Entry<Double, Double> lowerEntry = map.floorEntry(distanceMeters);
        Map.Entry<Double, Double> upperEntry = map.ceilingEntry(distanceMeters);

        if (lowerEntry == null || upperEntry == null) {
            return -1;
        }

        if (lowerEntry.getKey().equals(upperEntry.getKey())) {
            return lowerEntry.getValue();
        }

        double lowerDistance = lowerEntry.getKey();
        double upperDistance = upperEntry.getKey();
        double lowerValue = lowerEntry.getValue();
        double upperValue = upperEntry.getValue();

        return interpolate(lowerDistance, upperDistance, lowerValue, upperValue, distanceMeters);
    }

    private static double interpolate(
            double lowerKey,
            double upperKey,
            double lowerValue,
            double upperValue,
            double distanceMeters) {

        return ((upperValue - lowerValue) / (upperKey - lowerKey)) * (distanceMeters - lowerKey) + lowerValue;
    }

}