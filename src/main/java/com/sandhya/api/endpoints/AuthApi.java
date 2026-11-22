package com.sandhya.api.endpoints;

import com.sandhya.api.models.AuthRequest;
import com.sandhya.api.utils.RequestSpecFactory;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public final class AuthApi {

    private AuthApi() {
    }

    public static Response login(String username, String password) {
        return given().spec(RequestSpecFactory.credentialSpec())
                .body(new AuthRequest(username, password))
                .when().post("/auth");
    }

    public static String fetchToken(String username, String password) {
        Response response = login(username, password);
        if (response.statusCode() != 200) {
            throw new IllegalStateException("Authentication failed with HTTP status "
                    + response.statusCode() + ".");
        }

        String token = response.jsonPath().getString("token");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Authentication returned no token. Verify "
                    + "RESTFUL_BOOKER_USERNAME and RESTFUL_BOOKER_PASSWORD.");
        }
        return token;
    }
}
