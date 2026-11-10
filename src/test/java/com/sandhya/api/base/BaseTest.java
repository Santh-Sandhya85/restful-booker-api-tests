package com.sandhya.api.base;

import com.sandhya.api.config.ConfigReader;
import com.sandhya.api.endpoints.AuthApi;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    protected static String token;

    @BeforeSuite(alwaysRun = true)
    public void fetchAuthToken() {
        token = AuthApi.fetchToken(ConfigReader.username(), ConfigReader.password());
    }
}
