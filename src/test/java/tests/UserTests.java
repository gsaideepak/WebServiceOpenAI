package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.json.JSONObject;
import utils.AIPayloadClient;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class UserTests {

    @Test
    public void testCreateUser_ValidPayload() {
        RestAssured.baseURI = "https://reqres.in";
        JSONObject payload = AIPayloadClient.getPayload("POST /api/users", "valid");

        Response response = given()
            .header("Content-Type", "application/json")
            .body(payload.toString())
            .post("/api/users");

        response.then().log().all();
        assertEquals(response.getStatusCode(), 201);
    }

    @Test
    public void testCreateUser_InvalidPayload() {
        RestAssured.baseURI = "https://reqres.in";
        JSONObject payload = AIPayloadClient.getPayload("POST /api/users", "invalid");

        Response response = given()
            .header("Content-Type", "application/json")
            .body(payload.toString())
            .post("/api/users");

        response.then().log().all();
        assertEquals(response.getStatusCode(), 400); // or 422 based on actual response
    }
}@Test
    public void testGetSingleUser() {
        RestAssured.baseURI = "https://reqres.in";

        Response response = given()
            .get("/api/users/2");

        response.then().log().all();
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getInt("data.id"), 2);
    }

    @Test
    public void testUpdateUser_ValidPayload() {
        RestAssured.baseURI = "https://reqres.in";
        JSONObject payload = AIPayloadClient.getPayload("PUT /api/users/2", "valid");

        Response response = given()
            .header("Content-Type", "application/json")
            .body(payload.toString())
            .put("/api/users/2");

        response.then().log().all();
        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testDeleteUser() {
        RestAssured.baseURI = "https://reqres.in";

        Response response = given()
            .delete("/api/users/2");

        response.then().log().all();
        assertEquals(response.getStatusCode(), 204);
    }