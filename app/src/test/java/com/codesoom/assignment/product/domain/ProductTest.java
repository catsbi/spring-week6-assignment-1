package com.codesoom.assignment.product.domain;

import com.codesoom.assignment.product.errors.InvalidProductArgumentException;
import com.codesoom.assignment.product.provider.ProvideInvalidProductArguments;
import com.codesoom.assignment.product.provider.ProvideValidProductArguments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.util.ReflectionTestUtils;

import static com.codesoom.assignment.product.ConstantsForProductTest.PRODUCT_IMAGE_URL;
import static com.codesoom.assignment.product.ConstantsForProductTest.PRODUCT_MAKER;
import static com.codesoom.assignment.product.ConstantsForProductTest.PRODUCT_NAME;
import static com.codesoom.assignment.product.ConstantsForProductTest.PRODUCT_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Product 클래스")
class ProductTest {

    public static final String INIT_STRING_VALUE = "초기값";
    public static final int INIT_PRICE_VALUE = 1000;

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

    @DisplayName("update 메서드는")
    @Nested
    class Describe_update {
        private Product originProduct;

        @BeforeEach
        void setUp() {
            originProduct = Product.builder()
                    .name(INIT_STRING_VALUE)
                    .maker(INIT_STRING_VALUE)
                    .price(INIT_PRICE_VALUE)
                    .imageUrl(INIT_STRING_VALUE)
                    .build();
        }

        @DisplayName("인자가 유효하면, 정상적으로 수정된다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideValidProductArguments.class)
        void constructProduct(String name, String maker, Integer price, String imageUrl) {
            final Product target = new Product(name, maker, price, imageUrl);

            originProduct.update(target);

            assertThat(originProduct.getName()).isEqualTo(name);
            assertThat(originProduct.getMaker()).isEqualTo(maker);
            assertThat(originProduct.getPrice()).isEqualTo(price);
            assertThat(originProduct.getImageUrl()).isEqualTo(imageUrl);

        }

        @DisplayName("인자가 유효하지 않으면, 예외를 던진다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideInvalidProductArguments.class)
        void constructProductWithInvalidArguments(String name, String maker, Integer price, String imageUrl) {
            final Product invalidTarget = new Product();
            forceInjectProductArguments(name, maker, price, imageUrl, invalidTarget);

            assertThatThrownBy(() -> originProduct.update(invalidTarget))
                    .isInstanceOf(InvalidProductArgumentException.class);

        }

        private void forceInjectProductArguments(String name, String maker, Integer price, String imageUrl, Product invalidTarget) {
            ReflectionTestUtils.setField(invalidTarget, "name", name);
            ReflectionTestUtils.setField(invalidTarget, "maker", maker);
            ReflectionTestUtils.setField(invalidTarget, "price", price);
            ReflectionTestUtils.setField(invalidTarget, "imageUrl", imageUrl);
        }

    }

    @DisplayName("equals and hashCode 메서드는")
    @Nested
    class Describe_equals {

        private Product product;

        @BeforeEach
        void setUp() {
            product = Product.builder()
                    .name(PRODUCT_NAME)
                    .maker(PRODUCT_MAKER)
                    .price(PRODUCT_PRICE)
                    .imageUrl(PRODUCT_IMAGE_URL)
                    .build();
        }

        @DisplayName("동일성을 보장한다.")
        @Test
        void equalsSelf() {
            assertThat(product).isEqualTo(product);
        }

        @DisplayName("동등성을 보장한다.")
        @Test
        void equalsLogicalValue() {
            Product newProduct = Product.builder()
                    .name(PRODUCT_NAME)
                    .maker(PRODUCT_MAKER)
                    .price(PRODUCT_PRICE)
                    .imageUrl(PRODUCT_IMAGE_URL)
                    .build();

            assertThat(product).isEqualTo(newProduct);
        }

        @DisplayName("인자가 Product 타입이 아닌 경우 false를 반환한다.")
        @Test
        void equalsNotProductType() {
            assertThat(product)
                    .isNotEqualTo("String")
                    .isNotEqualTo(1000)
                    .isNotEqualTo(1000L)
                    .isNotEqualTo('C');
        }
    }


}
