package com.moshu.ssl.req;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class NewAccountReq {
    private String accountName;

    /**
     * 账户active权限的公钥
     */
    private String activePublicKey;
}