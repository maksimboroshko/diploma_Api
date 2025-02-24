package tests;

import base.TestBase;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import models.ProductRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.asynchttpclient.util.Assertions.assertNotNull;
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


    @Test
    public void getProductsTest() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/products");

        System.out.println("Full response body: " + response.getBody().asString());

        response.then()
                .spec(responseSpec)
                .body("[0].id", greaterThan(0))
                .body("[0].title", notNullValue())
                .body("[0].price", greaterThan(0.0f));
    }


    @Test
    public void createProductTest() {

        ProductRequest newProduct = new ProductRequest();
        newProduct.setTitle("Test Product");
        newProduct.setDescription("This is a test product");
        newProduct.setPrice(123);
        newProduct.setCategory("electronics");
        newProduct.setImage("https://example.com/test-product.jpg");


        Response response = given()
                .spec(requestSpec)
                .contentType("application/json")
                .body(newProduct)
                .when()
                .post("/products");
        System.out.println("Response body: " + response.getBody().asString());

        response.then()
                .statusCode(200) // Статус 201 фэйлит тест , можно как негативный добавить позже
                .body("title", equalTo(newProduct.getTitle()))
                .body("description", equalTo(newProduct.getDescription()))
                .body("price", equalTo(newProduct.getPrice()))
                .body("category", equalTo(newProduct.getCategory()))
                .body("image", equalTo(newProduct.getImage()));


        int productId = response.jsonPath().getInt("id");

        // Логирование ID продукта
        System.out.println("New product ID: " + productId);


        response.then()
                .body("id", greaterThan(0));
    }


    @Test
    public void updateProductTest() {

        ProductRequest newProduct = new ProductRequest();
        newProduct.setTitle("Test Product");
        newProduct.setDescription("Initial description");
        newProduct.setPrice(99);
        newProduct.setCategory("electronics");
        newProduct.setImage("https://example.com/test-product.jpg");

        Response createResponse = given()
                .spec(requestSpec)
                .contentType("application/json")
                .body(newProduct)
                .when()
                .post("/products");

        int productId = createResponse.jsonPath().getInt("id");


        System.out.println("Created product ID: " + productId);


        ProductRequest updatedProduct = new ProductRequest();
        updatedProduct.setTitle("Updated Product");
        updatedProduct.setDescription("Updated description");
        updatedProduct.setPrice(999);
        updatedProduct.setCategory("gadgets");
        updatedProduct.setImage("https://example.com/updated-product.jpg");

        Response updateResponse = given()
                .spec(requestSpec)
                .contentType("application/json")
                .body(updatedProduct)
                .when()
                .put("/products/" + productId);


        System.out.println("Updated product response: " + updateResponse.getBody().asString());


        updateResponse.then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("title", equalTo(updatedProduct.getTitle()))
                .body("description", equalTo(updatedProduct.getDescription()))
                .body("price", equalTo(updatedProduct.getPrice().intValue())) // убираем дробную часть для сравнения
                .body("category", equalTo(updatedProduct.getCategory()))
                .body("image", equalTo(updatedProduct.getImage()));
    }

    @Test
    public void createAndDeleteProductTest() {
        ProductRequest newProduct = new ProductRequest();
        newProduct.setTitle("Test Product");
        newProduct.setDescription("This is a test product");
        newProduct.setPrice(123);
        newProduct.setCategory("electronics");
        newProduct.setImage("https://example.com/test-product.jpg");


        Response response = given()
                .spec(requestSpec)
                .contentType("application/json")
                .body(newProduct)
                .when()
                .post("/products")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Response body: " + response.getBody().asString());

        int productId = response.jsonPath().getInt("id");
        System.out.println("Создан товар с ID: " + productId);

        // Удаляем товар
        given()
                .spec(requestSpec)
                .when()
                .delete("/products/" + productId)
                .then()
                .statusCode(200);

        System.out.println("Товар с ID " + productId + " успешно удалён.");
    }

    @Test
    public void filterProductsByCategoryTest() {

        String category = "electronics";


        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/products/category/" + category);
        System.out.println("Response body: " + response.getBody().asString());

        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", hasSize(greaterThan(0)))
                .body("every { it.category == '" + category + "' }", is(true));

        List<Map<String, Object>> products = response.jsonPath().getList("$");

        products.forEach(item -> {
            assertNotNull(item.get("id"), "Product ID should not be null");
            assertNotNull(item.get("title"), "Product title should not be null");
            assertNotNull(item.get("price"), "Product price should not be null");
            assertNotNull(item.get("category"), "Product category should not be null");
            assertNotNull(item.get("image"), "Product image should not be null");
        });


    }
}

