package com.restapi.mobileappws.restAssured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserWebServiceEndpointTest {

    private final String CONTEXT_PATH = "/mobile-app-ws";
    private final String EMAIL_ADDRESS = "bandipotaiye@gmail.com";
    private final String JSON = "application/json";

    @BeforeEach
    void setUp(){

        RestAssured.baseURI="http://localhost";
        RestAssured.port=2009;
    }

    @Test
    void testUserLogin(){
        //First create the request PayLoad of the login endpoint
        Map<String,String> loginDetails = new HashMap<>();

        loginDetails.put("email", EMAIL_ADDRESS);
        loginDetails.put("password", "taiye12345");

        //creating the Http request with RestAssured

        Response response = given().contentType(JSON)// the endpoint content type
                .accept(JSON) // accept type
                .body(loginDetails) // that this request body will consist of the payload
                .when().post(CONTEXT_PATH + "/users/login")  //When Http post request is sent to this url
                .then().statusCode(200) // then return 200 status code
                .extract().response();// extract the response

            // user the response object to read the Authorization

        String authorizationHeader = response.header("Authorization");
        String userId = response.header("UserID");

//        assertNotNull(authorizationHeader);
        assertNotNull(userId);
    }
}
