package specs;

import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;

public class RequestSpecs {

    public static RequestSpecification requestSpec = RestAssured.given()
            .log().all();
}
