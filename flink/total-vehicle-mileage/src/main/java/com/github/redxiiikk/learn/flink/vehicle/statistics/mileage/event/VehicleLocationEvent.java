package com.github.redxiiikk.learn.flink.vehicle.statistics.mileage.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleLocationEvent {
    private String id;
    private double longitude;
    private double latitude;
}
