package com.moshu.ssl.demo;

import com.moshu.ssl.util.HttpsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class BaseDemo {

    @Value("${global.url}")
    protected String URL;


    @Autowired
    protected HttpsUtil httpsUtil;
}
