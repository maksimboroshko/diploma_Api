package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.ProductRequest;
import models.ProductResponse;
import org.junit.jupiter.api.Test;
import base.TestBase;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.RequestSpecs.requestSpec;
import static specs.ResponseSpecs.responseSpec;

@Feature("API Тесты для продуктов")
public class ProductTests extends TestBase {

    @Test
    @Story("Создание нового продукта")
    void createNewProductTest() {
        ProductRequest newProduct = new ProductRequest("Test Product", "A test description", 99.99, "electronics", "https://example.com/image.png");

        ProductResponse createdProduct = given()
                .spec(requestSpec)
                .body(newProduct)
                .when()
                .post("/products")
                .then()
                .spec(responseSpec)
                .extract().as(ProductResponse.class);

        assertThat(createdProduct.getTitle()).isEqualTo(newProduct.getTitle());
        assertThat(createdProduct.getPrice()).isEqualTo(newProduct.getPrice());
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
}
