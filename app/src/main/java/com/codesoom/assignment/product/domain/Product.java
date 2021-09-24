package com.codesoom.assignment.product.domain;

import com.codesoom.assignment.product.errors.InvalidProductArgumentException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

import static com.codesoom.assignment.product.infra.Validators.isValidNumber;
import static com.codesoom.assignment.product.infra.Validators.isValidString;

/**
 * 상품 정보
 */
@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String maker;

    private Integer price;

    private String imageUrl;

    public Product(String name, String maker, Integer price, String imageUrl) {
        validCheckOrElseThrows(name, maker, price, imageUrl);

        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    private void validCheckOrElseThrows(String name, String maker, Integer price, String imageUrl) {
        if (!isValidString(name)) {
            throw new InvalidProductArgumentException(name);
        }
        if (!isValidString(maker)) {
            throw new InvalidProductArgumentException(maker);
        }
        if (!isValidString(imageUrl)) {
            throw new InvalidProductArgumentException(imageUrl);
        }
        if (!isValidNumber(price)) {
            throw new InvalidProductArgumentException(price);
        }
    }

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
