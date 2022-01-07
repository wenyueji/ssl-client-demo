package com.moshu.ssl.req;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class TrxCommitReq {
    /**
     * EOS账户名字
     */
    private String accountName;
    /**
     * EOS合约托管账户名字
     */
    private String contractName;
    /**
     * 调用的EOS智能合约方法
     */
    private String contractAction;
    /**
     * 上链参数
     */
    private Map<String, Object> args;

}
