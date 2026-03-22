package frc.robot.util;

import java.util.Map;
import java.util.TreeMap;

public class ShooterLookup {

    private static final TreeMap<Double, Double> velocityMap = new TreeMap<>();

    public ShooterLookup()
    {
        // example values:
        // key: distance to target in meters
        // value: shooter wheel velocity 
        velocityMap.put(1.0, -5.0);
        velocityMap.put(2.0, -10.0);
        velocityMap.put(3.0, -15.0);
        velocityMap.put(4.0, -20.0);
        velocityMap.put(5.0, -25.0);
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