package com.hdbank.productservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Document(value = "product")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;

    // Constructor mặc định (Thay thế @NoArgsConstructor)
    public Product() {
    }

    // Constructor đầy đủ tham số (Thay thế @AllArgsConstructor)
    public Product(String id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Cung cấp một static builder đơn giản để không phải sửa file ProductService
    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private String name;
        private String description;
        private BigDecimal price;

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.setName(this.name);
            product.setDescription(this.description);
            product.setPrice(this.price);
            return product;
        }
    }

    // Các hàm Getter và Setter thủ công (Thay thế @Data)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}