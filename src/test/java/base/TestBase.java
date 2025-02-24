package base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


public class TestBase {

    static {
        RestAssured.baseURI = "https://fakestoreapi.com";
    }

    public static RequestSpecification requestSpec = RestAssured.given()
            .log().all()
            .filter(new AllureRestAssured())
            .contentType("application/json");

    public static ResponseSpecification responseSpec = RestAssured.expect()
            .log().all()
            .statusCode(200);
        }

