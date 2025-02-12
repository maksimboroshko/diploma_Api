package specs;

import io.restassured.specification.ResponseSpecification;
import io.restassured.RestAssured;

public class ResponseSpecs {

    public static ResponseSpecification responseSpec = RestAssured.expect()
            .log().all();
}
