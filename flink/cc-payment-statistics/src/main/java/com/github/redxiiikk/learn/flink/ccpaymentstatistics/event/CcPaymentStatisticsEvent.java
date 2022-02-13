package com.github.redxiiikk.learn.flink.ccpaymentstatistics.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CcPaymentStatisticsEvent {
    private String merchantId;
    private double amount;
}
