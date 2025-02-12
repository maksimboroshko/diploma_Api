package tests;
import io.restassured.response.Response;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import base.TestBase;
import models.ProductRequest;
import org.hamcrest.Matchers;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;

@Feature("API Тесты для продуктов")
public class ProductTests extends TestBase {
    @Test
    @Story("Получение всех продуктов")
    void getAllProductsTest() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products")
                .then()
                .spec(responseSpec)
                .statusCode(200);
    }

    // Тест на проверку получения списка продуктов (GET /products)
    // Тест на проверку получения списка продуктов (GET /products)
    @Test
    public void getProductsTest() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/products");

        // Логирование всего тела ответа
        System.out.println("Full response body: " + response.getBody().asString());

        response.then()
                .spec(responseSpec)
                .body("[0].id", greaterThan(0))
                .body("[0].title", notNullValue())
                .body("[0].price", greaterThan(0.0f));
    }


    @Test
    public void createProductTest() {
        // Создание объекта запроса (ProductRequest)
        ProductRequest newProduct = new ProductRequest();
        newProduct.setTitle("Test Product");
        newProduct.setDescription("This is a test product");
        newProduct.setPrice(new BigDecimal("123.0"));
        newProduct.setCategory("electronics");
        newProduct.setImage("https://example.com/test-product.jpg");

        // Отправка POST-запроса для создания нового продукта
        Response response = given()
                .spec(requestSpec) // используем вашу спецификацию запроса
                .contentType("application/json")
                .body(newProduct) // передаем объект продукта в теле запроса
                .when()
                .post("/products");

        // Логирование полного ответа
        System.out.println("Response body: " + response.getBody().asString());

        // Получаем цену из ответа как число (Double) для точности
        Double priceFromResponse = response.jsonPath().getDouble("price");

        // Сравниваем как Double с Double
        response.then()
                .statusCode(200) // Статус 200 фэйлит тест
                .body("title", equalTo(newProduct.getTitle()))
                .body("description", equalTo(newProduct.getDescription()))
                .body("price", equalTo(newProduct.getPrice().doubleValue())) // Сравнение как Double
                .body("category", equalTo(newProduct.getCategory()))
                .body("image", equalTo(newProduct.getImage()));

        // Получение ID нового продукта из ответа и проверка
        int productId = response.jsonPath().getInt("id");

        // Логирование ID продукта
        System.out.println("New product ID: " + productId);

        // Дополнительная проверка, что ID был возвращен
        response.then()
                .body("id", greaterThan(0));
    }
}