package com.sandhya.api.tests;

import com.sandhya.api.base.BaseTest;
import com.sandhya.api.endpoints.AuthApi;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class AuthTests extends BaseTest {

    @Test(description = "Valid credentials return a token")
    @Description("POST /auth with configured credentials returns 200 and a non-empty token")
    public void validCredentialsReturnToken() {
        Assert.assertFalse(token.isBlank(), "suite authentication should provide a token");
    }

    @Test(description = "Invalid credentials return a Bad credentials reason")
    public void invalidCredentialsAreRejected() {
        // Restful-Booker returns 200 with a "reason" field instead of 401
        Response response = AuthApi.login("invalid-user", "invalid-password");
        response.then().statusCode(200)
                .body("reason", equalTo("Bad credentials"))
                .body("token", nullValue());
    }
}
