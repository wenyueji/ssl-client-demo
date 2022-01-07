package com.moshu.ssl.req;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Validated
public class FreezeContractReq {

    private String accountName;
    private String activePublicKey;
    private Long ram;
    private BigDecimal cpu;
    private BigDecimal net;

}
