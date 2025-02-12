package base;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import listners.AllureListener;


public class TestBase {

    static {
        RestAssured.baseURI = "https://fakestoreapi.com";
    }

    public static RequestSpecification requestSpec = RestAssured.given()
            .log().all() // Убрали LogDetail.ALL
            .filter(new AllureListener());

    public static ResponseSpecification responseSpec = RestAssured.expect()
            .log().all();
}

