package com.codesoom.assignment.product.domain;

import com.codesoom.assignment.product.errors.InvalidProductArgumentException;
import com.codesoom.assignment.product.provider.ProvideInvalidProductArguments;
import com.codesoom.assignment.product.provider.ProvideValidProductArguments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Product 클래스")
class ProductTest {

    @DisplayName("생성자는")
    @Nested
    class Describe_constructor {

        @DisplayName("인자가 유효하면, 정상적으로 생성된다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideValidProductArguments.class)
        void constructProduct(String name, String maker, Integer price, String imageUrl) {
            final Product newProduct = new Product(name, maker, price, imageUrl);

            assertThat(newProduct).isEqualTo(new Product(name, maker, price, imageUrl));
            assertThat(newProduct.getName()).isEqualTo(name);
            assertThat(newProduct.getMaker()).isEqualTo(maker);
            assertThat(newProduct.getPrice()).isEqualTo(price);
            assertThat(newProduct.getImageUrl()).isEqualTo(imageUrl);

        }

        @DisplayName("인자가 유효하지 않으면, 예외를 던진다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideInvalidProductArguments.class)
        void constructProductWithInvalidArguments(String name, String maker, Integer price, String imageUrl) {

            assertThatThrownBy(() -> new Product(name, maker, price, imageUrl))
                    .isInstanceOf(InvalidProductArgumentException.class);

        }


    }


}
