package com.moshu.ssl.req;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class FreezeContractNewReq {

    private String accountName;
}
