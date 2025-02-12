package tests;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.ProductRequest;
import models.ProductResponse;
import org.junit.jupiter.api.Test;
import base.TestBase;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

@Feature("API Тесты для продуктов")
public class ProductTests extends TestBase {

    @Test
    @Story("Создание нового продукта")
    void createNewProductTest() {
        // Создаем объект запроса
        ProductRequest newProduct = new ProductRequest(
                "Test Product",
                "A test description",
                99.99,
                "electronics",
                "https://example.com/image.png"
        );

        // Отправляем запрос и получаем ответ в виде объекта ProductResponse
        ProductResponse createdProduct = given()
                .spec(requestSpec)
                .body(newProduct)
                .when()
                .post("/products")
                .then()
                .spec(responseSpec)
                .extract().as(ProductResponse.class);

        // Проверяем, что данные совпадают
        assertThat(createdProduct.getTitle()).isEqualTo(newProduct.getTitle());
        assertThat(createdProduct.getPrice()).isEqualTo(newProduct.getPrice());
        assertThat(createdProduct.getDescription()).isEqualTo(newProduct.getDescription());
        assertThat(createdProduct.getCategory()).isEqualTo(newProduct.getCategory());
        assertThat(createdProduct.getImage()).isEqualTo(newProduct.getImage());

        // Проверяем, что API вернул ID, так как в ответе он был (например, 21)
        assertThat(createdProduct.getId()).isNotNull().isGreaterThan(0);
    }

    @Test
    @Story("Получение информации о продукте")
    void getProductByIdTest() {
        int productId = 1;

        ProductResponse product = given()
                .spec(requestSpec)
                .when()
                .get("/products/" + productId)
                .then()
                .spec(responseSpec)
                .extract().as(ProductResponse.class);

        assertThat(product.getId()).isEqualTo(productId);
    }

    @Test
    @Story("Удаление продукта")
    void deleteProductTest() {
        int productId = 1;

        given()
                .spec(requestSpec)
                .when()
                .delete("/products/" + productId)
                .then()
                .spec(responseSpec)
                .statusCode(200);
    }

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
    @Story("Обновление продукта")
    void updateProductTest() {
        int productId = 1;
        ProductRequest updatedProduct = new ProductRequest("Updated Product", "Updated description", 79.99, "electronics", "https://example.com/updated-image.png");

        ProductResponse product = given()
                .spec(requestSpec)
                .body(updatedProduct)
                .when()
                .put("/products/" + productId)
                .then()
                .spec(responseSpec)
                .extract().as(ProductResponse.class);

        assertThat(product.getTitle()).isEqualTo(updatedProduct.getTitle());
        assertThat(product.getPrice()).isEqualTo(updatedProduct.getPrice());
    }

    @Test
    public void createNewProductTest1() {
        String requestBody = """
            {
                "title": "Test Product",
                "description": "A test description",
                "price": 99.99,
                "category": "electronics",
                "image": "https://example.com/image.png"
            }
        """;

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("https://fakestoreapi.com/products")
                .then()
                .statusCode(200) // Убедимся, что API вернул успешный код
                .extract().response();

        // Извлекаем title из JSON-ответа
        String actualTitle = response.jsonPath().getString("title");

        // Проверяем, что title в ответе совпадает с ожидаемым
        assertThat(actualTitle, is("Test Product"));
    }
}
