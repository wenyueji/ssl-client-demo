package com.moshu.ssl.req;

import lombok.*;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class EosResourceRecoveryReq {

    private String accountName;

    private Long ram = 0L;

    private BigDecimal net = BigDecimal.ZERO;

    private BigDecimal cpu = BigDecimal.ZERO;


}
