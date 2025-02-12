package specs;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;

public class RequestSpecs {

    public static RequestSpecification requestSpec = RestAssured.given()
            .log().all()
            .header("Content-Type", "application/json")
            .filter(new AllureRestAssured());

}
