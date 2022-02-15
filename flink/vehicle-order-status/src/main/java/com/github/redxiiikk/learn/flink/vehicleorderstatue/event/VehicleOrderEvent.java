package com.github.redxiiikk.learn.flink.vehicleorderstatue.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleOrderEvent {
    private String id;
    private String vehicleNumber;
    private String statue;
}
