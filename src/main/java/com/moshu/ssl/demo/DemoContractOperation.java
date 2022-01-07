package com.moshu.ssl.demo;

import com.moshu.ssl.req.FreezeContractNewReq;
import com.moshu.ssl.req.FreezeContractReq;
import com.moshu.ssl.util.JSONUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class DemoContractOperation extends BaseDemo {

    public void deployContract() throws Exception {

        Map<String, String> filesMap = new HashMap<String, String>();
        Map<String, String> fieldMap = new HashMap<String, String>();
        fieldMap.put("userName", "a.gif");
        filesMap.put("wasmFile","/null.wasm" );
        filesMap.put("abiFile","/null.abi" );



        httpsUtil.httpsFileUpload(URL +"/contract/deployContract",fieldMap,filesMap);
//        httpsUtil.httpsPost(URL + "/contract/deployContract", JSONUtil.objectToJsonStr(""));
    }

    public void freezeContract(FreezeContractNewReq req) throws Exception {
        httpsUtil.httpsPost(URL + "/contract/freezeContract", JSONUtil.objectToJsonStr(req));
    }

    public void unFreezeContract(FreezeContractReq req) throws Exception {
        httpsUtil.httpsPost(URL + "/contract/unFreezeContract", JSONUtil.objectToJsonStr(req));
    }


}
