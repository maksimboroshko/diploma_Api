package listners;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.Filter;

public class AllureListener {
    public static Filter getAllureListener() {
        return new AllureRestAssured();
    }
}
