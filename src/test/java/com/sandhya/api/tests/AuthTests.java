package com.sandhya.api.tests;

import com.sandhya.api.config.ConfigReader;
import com.sandhya.api.endpoints.AuthApi;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class AuthTests {

    @Test(description = "Valid credentials return a token")
    @Description("POST /auth with valid admin credentials returns 200 and a non-empty token")
    public void validCredentialsReturnToken() {
        AuthApi.login(ConfigReader.username(), ConfigReader.password())
                .then().statusCode(200)
                .body("token", not(emptyOrNullString()));
    }

    @Test(description = "Invalid credentials return a Bad credentials reason")
    public void invalidCredentialsAreRejected() {
        // Restful-Booker returns 200 with a "reason" field instead of 401
        Response response = AuthApi.login("admin", "wrong-password");
        response.then().statusCode(200)
                .body("reason", equalTo("Bad credentials"))
                .body("token", nullValue());
    }
}
