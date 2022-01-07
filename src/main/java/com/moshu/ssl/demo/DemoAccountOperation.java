package com.moshu.ssl.demo;


import com.moshu.ssl.req.EosResourceRecoveryReq;
import com.moshu.ssl.req.EosResourceReq;
import com.moshu.ssl.req.NewAccountReq;
import com.moshu.ssl.util.JSONUtil;
import org.springframework.stereotype.Service;

@Service
public class DemoAccountOperation extends BaseDemo {

    public void createAcount(NewAccountReq req) throws Exception {

        String params = JSONUtil.objectToJsonStr(req);
        httpsUtil.httpsPost(URL + "/account/create", params);
    }

    public void getAcount(String accountName) throws Exception {
        httpsUtil.httpsGet(URL + "/account/getAccount?accountName=" + accountName);
    }

    public void addResource(EosResourceReq req) throws Exception {
        httpsUtil.httpsPost(URL + "/account/resource/add", JSONUtil.objectToJsonStr(req));
    }

    public void resourceRecovery(EosResourceRecoveryReq req) throws Exception {
        httpsUtil.httpsPost(URL + "/account/resource/recovery", JSONUtil.objectToJsonStr(req));
    }
}
