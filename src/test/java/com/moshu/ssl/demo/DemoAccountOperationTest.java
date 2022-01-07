package com.moshu.ssl.demo;

import com.moshu.ssl.req.EosResourceReq;
import com.moshu.ssl.req.NewAccountReq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class DemoAccountOperationTest {

    @Autowired
    DemoAccountOperation demoAccount;

    @Test
    void testGetAccount() throws Exception {
        demoAccount.getAcount("aaaaaaaaaaaa");
    }

    @Test
    public void testCreateAccount() throws Exception {
        NewAccountReq req = new NewAccountReq("123", "123");
        demoAccount.createAcount(req);
    }

    @Test
    public void testAddResource() throws Exception {
        EosResourceReq req = new EosResourceReq("abcd1234", 60000L, new BigDecimal(10), new BigDecimal(10));
        demoAccount.addResource(req);
    }
}