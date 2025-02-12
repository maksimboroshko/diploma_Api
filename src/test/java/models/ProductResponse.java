package models;

import lombok.Data;

@Data
public class ProductResponse {
    private int id;
    private String title;
    private String description;
    private double price;
    private String category;
    private String image;
}
