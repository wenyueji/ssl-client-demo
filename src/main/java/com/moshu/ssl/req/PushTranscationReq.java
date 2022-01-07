package com.moshu.ssl.req;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class PushTranscationReq {

    private String accountName;

    private String contractName;

    private String contractAction;

    private Map<String,Object> args;

}
