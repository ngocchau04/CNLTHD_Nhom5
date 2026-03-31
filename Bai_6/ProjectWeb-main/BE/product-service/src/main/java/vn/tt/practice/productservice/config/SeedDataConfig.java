package vn.tt.practice.productservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.tt.practice.productservice.model.Product;
import vn.tt.practice.productservice.repository.ProductRepo;

import java.util.List;

@Configuration
public class SeedDataConfig {

    @Bean
    CommandLineRunner seedProducts(ProductRepo productRepo) {
        return args -> {
            if (productRepo.count() > 0) {
                return;
            }

            List<Product> seed = List.of(
                    Product.builder()
                            .name("Headphone A")
                            .price(1990000)
                            .description("Demo product")
                            .image("https://drive.google.com/thumbnail?id=1Y1dhTKw9qtFNYq06zj2CjtGCiUd0tmxS&sz=w800")
                            .rating(5)
                            .quantity(20)
                            .productCode("HP-A")
                            .checkToCart(false)
                            .build(),
                    Product.builder()
                            .name("Headphone B")
                            .price(1490000)
                            .description("Demo product")
                            .image("https://drive.google.com/thumbnail?id=1ELU99yqZDsKXYYXsB2CmbDu6GtrHH9hA&sz=w800")
                            .rating(5)
                            .quantity(20)
                            .productCode("HP-B")
                            .checkToCart(false)
                            .build(),
                    Product.builder()
                            .name("Headphone C")
                            .price(990000)
                            .description("Demo product")
                            .image("https://drive.google.com/thumbnail?id=1HxEq9fIVQwGscOH5IvOja0MrlMiEI1f5&sz=w800")
                            .rating(5)
                            .quantity(20)
                            .productCode("HP-C")
                            .checkToCart(false)
                            .build(),
                    Product.builder()
                            .name("Headphone D")
                            .price(2590000)
                            .description("Demo product")
                            .image("https://drive.google.com/thumbnail?id=1mjiYi17R05DUsURBI_e8VOinq-I5-lxu&sz=w800")
                            .rating(5)
                            .quantity(20)
                            .productCode("HP-D")
                            .checkToCart(false)
                            .build(),
                    Product.builder()
                            .name("Headphone E")
                            .price(1790000)
                            .description("Demo product")
                            .image("https://drive.google.com/thumbnail?id=1L3CPH43OPsnTLL13bh3RJQOg0zdYawwl&sz=w800")
                            .rating(5)
                            .quantity(20)
                            .productCode("HP-E")
                            .checkToCart(false)
                            .build()
            );

            productRepo.saveAll(seed);
        };
    }
}
