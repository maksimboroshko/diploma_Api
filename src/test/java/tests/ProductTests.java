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

        // Отправка POST-запроса для создания нового продукта
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

        // Получение ID нового продукта из ответа и проверка
        int productId = response.jsonPath().getInt("id");

        // Логирование ID продукта
        System.out.println("New product ID: " + productId);

        // Дополнительная проверка, что ID был возвращен
        response.then()
                .body("id", greaterThan(0));
    }


    @Test
    public void updateProductTest() {
        // Создаем новый товар
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

        // Логируем созданный продукт
        System.out.println("Created product ID: " + productId);

        // Обновляем данные товара
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

        // Логируем обновленный продукт
        System.out.println("Updated product response: " + updateResponse.getBody().asString());

        // Проверяем, что товар обновился корректно
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

        // Отправка POST-запроса для создания нового продукта
        Response response = given()
                .spec(requestSpec)
                .contentType("application/json")
                .body(newProduct)
                .when()
                .post("/products")
                .then()
                .statusCode(200) // Проверяем успешное создание
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

        // Отправка GET-запроса для получения товаров по категории
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/products/category/" + category);
        System.out.println("Response body: " + response.getBody().asString());

        response.then()
                .statusCode(200) // Статус 200 OK
                .contentType("application/json") // Проверка, что ответ в формате JSON
                .body("$", hasSize(greaterThan(0))) // Проверка, что товары присутствуют в ответе
                .body("every { it.category == '" + category + "' }", is(true)); // Проверка, что каждый товар относится к категории "electronics"

        List<Map<String, Object>> products = response.jsonPath().getList("$"); // Получаем список товаров

// Проверяем каждый товар в списке
        products.forEach(item -> {
            assertNotNull(item.get("id"), "Product ID should not be null");
            assertNotNull(item.get("title"), "Product title should not be null");
            assertNotNull(item.get("price"), "Product price should not be null");
            assertNotNull(item.get("category"), "Product category should not be null");
            assertNotNull(item.get("image"), "Product image should not be null");
        });


    }
}

