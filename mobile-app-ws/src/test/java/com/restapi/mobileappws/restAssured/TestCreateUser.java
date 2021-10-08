package com.restapi.mobileappws.restAssured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class TestCreateUser {

    private final String CONTEXT_PATH = "/mobile-app-ws";

    @BeforeEach
    void setUp(){
        RestAssured.baseURI="http://localhost";
        RestAssured.port=2009;
    }

    @Test
    void createUser(){

        // First we model the request

        List<Map<String, Object>> userAddresses = new ArrayList<>();

        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("city", "Lagos");
        shippingAddress.put("country", "Nigeria");
        shippingAddress.put("streetName", "Asajon way");
        shippingAddress.put("postalCode", "250202");
        shippingAddress.put("type","shipping");

        Map<String, Object> billingAddress = new HashMap<>();
        billingAddress.put("city", "Lagos");
        billingAddress.put("country", "Nigeria");
        billingAddress.put("streetName", "Asajon way");
        billingAddress.put("postalCode", "250202");
        billingAddress.put("type","shipping");

        userAddresses.add(shippingAddress);
        userAddresses.add(billingAddress);


        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "Taiye");
        userDetails.put("lastName", "Bandipo");
        userDetails.put("email", "bandipotaiye@gmail.com");
        userDetails.put("password", "taiye12345");
        userDetails.put("addresses", userAddresses);


// then we make the request
        Response response = given()
                .contentType("application/json") //set contentType
                .accept("application/json") // set acceptType
                .body(userDetails)
                .when() //when the end point
                .post(CONTEXT_PATH + "/users")//this end point is called
                .then()// then
                .statusCode(200)// return status code 200
                .contentType("application/json")
                .extract()
                .response();

        // we get userId  and email from the response header

        String userId = response.jsonPath().get("userId");

        String userEmail = response.jsonPath().get("email");


        assertAll(
                ()->{
                    assertNotNull(userId);
                    assertEquals(25, userId.length());
                    assertEquals(userDetails.get("email"), userEmail);
                }
        );


        // Get the response body

        String bodyString = response.body().asString();

        //then convert the body to Json object

        try {

            JSONObject responseBodyJson = new JSONObject(bodyString); // convert the bodyString to JSONObject

            //get the addresses which is an array
            JSONArray addresses = responseBodyJson.getJSONArray("addresses");//line 47





            assertNotNull(addresses);
            assertEquals(2, addresses.length());

            //get the addressId of the first AddressResponse
            String addressId = addresses.getJSONObject(0).getString("addressId");


            assertAll(
                    ()-> {
                        assertNotNull(addressId);
                        assertEquals(25, addressId.length());
                    }
            );


        } catch (JSONException e) {
            fail(e.getMessage());// the test should fail if the assertions are false
        }

    }

}
