package com.github.redxiiikk.learn.flink.ccpaymentstatistics.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CcPaymentStatisticsEvent {
    private String merchantId;
    private double amount;
}
