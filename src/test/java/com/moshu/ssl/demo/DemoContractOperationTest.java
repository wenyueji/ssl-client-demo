package com.moshu.ssl.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class DemoContractOperationTest {

    @Autowired
    DemoContractOperation demoContractOperation;

    @Test
    void testDeployContract() throws Exception {
        demoContractOperation.deployContract();
    }
}