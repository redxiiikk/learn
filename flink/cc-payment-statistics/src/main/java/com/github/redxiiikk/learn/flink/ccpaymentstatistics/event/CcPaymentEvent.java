package com.github.redxiiikk.learn.flink.ccpaymentstatistics.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public final class CcPaymentEvent {
    private String id;
    private String time;
    private double amount;
    private String currency;
    private String creditCardId;
    private String merchantId;

    public static CcPaymentStatisticsEvent to(CcPaymentEvent event) {
        return new CcPaymentStatisticsEvent(event.getMerchantId(), event.getAmount());
    }
}

