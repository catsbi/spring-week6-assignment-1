package com.codesoom.assignment.product.domain;

import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String maker;

    private Integer price;

    private String imageUrl;

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id)
                && Objects.equals(name, product.name)
                && Objects.equals(maker, product.maker)
                && Objects.equals(price, product.price)
                && Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(id, name, maker, price, imageUrl);
    }
}
