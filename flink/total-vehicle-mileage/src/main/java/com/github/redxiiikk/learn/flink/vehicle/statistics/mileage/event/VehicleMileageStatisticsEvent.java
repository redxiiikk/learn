package com.github.redxiiikk.learn.flink.vehicle.statistics.mileage.event;

import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

@Getter
@Setter
public class VehicleMileageStatisticsEvent {
    private String orderId;
    private String vehicleNumber;
    private double totalMileage;
    private VehicleLocationEvent lastLocation;

    public VehicleMileageStatisticsEvent(String orderId, String vehicleNumber, VehicleLocationEvent lastLocation) {
        this.orderId = orderId;
        this.vehicleNumber = vehicleNumber;
        this.lastLocation = lastLocation;
    }

    public VehicleMileageStatisticsEvent updateLocation(VehicleLocationEvent event) {
        if (lastLocation != null) {
            totalMileage = totalMileage + sqrt(
                    pow(event.getLatitude() - lastLocation.getLatitude(), 2)
                            + pow(event.getLongitude() - lastLocation.getLongitude(), 2));
        }
        lastLocation = event;
        return this;
    }
}
