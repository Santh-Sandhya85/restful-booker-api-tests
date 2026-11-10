package com.sandhya.api.endpoints;

import com.sandhya.api.models.AuthRequest;
import com.sandhya.api.utils.RequestSpecFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public final class AuthApi {

    private AuthApi() {
    }

    @Step("POST /auth")
    public static Response login(String username, String password) {
        return given().spec(RequestSpecFactory.baseSpec())
                .body(new AuthRequest(username, password))
                .when().post("/auth");
    }

    public static String fetchToken(String username, String password) {
        return login(username, password).then().statusCode(200).extract().path("token");
    }
}
