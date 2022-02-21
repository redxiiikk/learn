package com.github.redxiiikk.learn.flink.vehicle.statistics.mileage.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VehicleOrderEvent {
    private String id;
    private String vehicleNumber;
    private String statue;
}
