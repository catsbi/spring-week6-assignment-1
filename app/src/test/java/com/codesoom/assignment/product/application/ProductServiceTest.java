package com.codesoom.assignment.product.application;

import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.domain.ProductList;
import com.codesoom.assignment.product.domain.ProductRepository;
import com.codesoom.assignment.product.dto.ProductSaveData;
import com.codesoom.assignment.product.dto.ProductUpdateData;
import com.codesoom.assignment.product.errors.InvalidProductArgumentException;
import com.codesoom.assignment.product.errors.ProductNotFoundException;
import com.codesoom.assignment.product.provider.ProvideInvalidProductArguments;
import com.codesoom.assignment.product.provider.ProvideValidProductArguments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static com.codesoom.assignment.product.ConstantsForProductTest.PRODUCT_IMAGE_URL;
import static com.codesoom.assignment.product.ConstantsForProductTest.PRODUCT_MAKER;
import static com.codesoom.assignment.product.ConstantsForProductTest.PRODUCT_NAME;
import static com.codesoom.assignment.product.ConstantsForProductTest.PRODUCT_PRICE;
import static com.codesoom.assignment.product.ConstantsForProductTest.두더지잡기;
import static com.codesoom.assignment.product.ConstantsForProductTest.스크래쳐;
import static com.codesoom.assignment.product.ConstantsForProductTest.츄르;
import static com.codesoom.assignment.product.ConstantsForProductTest.펫모닝;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ProductService 클래스")
@DataJpaTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    private ProductService productService;

    private List<Product> products;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        productService = new ProductService(productRepository);

        products = productRepository.saveAll(Arrays.asList(츄르, 스크래쳐, 펫모닝, 두더지잡기));
    }


    @DisplayName("findAllProduct 메서드는")
    @Nested
    class Describe_findAllProduct {
        @DisplayName("등록된 상품이 있을 경우 상품 목록을 반환한다.")
        @Test
        void findAllWithRegisteredProducts() {
            final ProductList foundProducts = productService.findAllProduct();

            assertThat(foundProducts.isEmpty()).isFalse();
            assertThat(foundProducts.size()).isEqualTo(products.size());
        }

        @DisplayName("등록된 상품이 없을 경우 빈 목록을 반환한다.")
        @Test
        void findAllWithoutRegisteredProducts() {
            productRepository.deleteAll();

            final ProductList foundProducts = productService.findAllProduct();

            assertThat(foundProducts.isEmpty()).isTrue();
        }
    }

    @DisplayName("findProduct 메서드는")
    @Nested
    class Describe_findProduct {
        @DisplayName("존재하는 상품의 식별자를 조회하면, 상품 상세정보를 반환한다.")
        @Test
        void findProductWithExistsId() {
            for (Product product : products) {
                final Product foundProduct = productService.findProduct(product.getId());

                assertThat(foundProduct).isEqualTo(product);
            }
        }

        @DisplayName("존재하지 않는 상품의 식별자를 조회하면, 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(longs = {Long.MAX_VALUE, Long.MIN_VALUE, 0L, 999L, 1999L, 1199L, 99999L})
        void findProductWithNotExistsId(Long invalidId) {
            assertThatThrownBy(() -> productService.findProduct(invalidId))
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessage(String.format(ProductNotFoundException.DEFAULT_MESSAGE, invalidId));
        }
    }

    @DisplayName("createProduct 메서드는")
    @Nested
    class Describe_createProduct {
        @DisplayName("인자가 유효할 경우, 상품을 등록한 뒤 반환한다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideValidProductArguments.class)
        void createProductWithValidData(String name, String maker, Integer price, String imageUrl) {
            final ProductSaveData saveData = ProductSaveData.builder()
                    .name(name)
                    .maker(maker)
                    .price(price)
                    .imageUrl(imageUrl)
                    .build();

            final Product savedProduct = productService.createProduct(saveData);

            assertThat(savedProduct.getName()).isEqualTo(name);
            assertThat(savedProduct.getMaker()).isEqualTo(maker);
            assertThat(savedProduct.getPrice()).isEqualTo(price);
            assertThat(savedProduct.getImageUrl()).isEqualTo(imageUrl);
        }

        @DisplayName("인자가 유효하지 않을 경우, 예외를 던진다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideInvalidProductArguments.class)
        void createProductWithInvalidData(String name, String maker, Integer price, String imageUrl) {
            final ProductSaveData saveData = ProductSaveData.builder()
                    .name(name)
                    .maker(maker)
                    .price(price)
                    .imageUrl(imageUrl)
                    .build();

            assertThatThrownBy(() -> productService.createProduct(saveData))
                    .isInstanceOf(InvalidProductArgumentException.class);
        }
    }

    @DisplayName("updateProduct 메서드는")
    @Nested
    class Describe_updateProduct {
        @DisplayName("존재하는 상품의 식별자로 수정하려 하는 경우")
        @Nested
        class Context_with_exists_product {
            @DisplayName("인자가 유효할 경우, 상품을 수정한 뒤 반환한다.")
            @ParameterizedTest
            @ArgumentsSource(ProvideValidProductArguments.class)
            void updateProductWithValidData(String name, String maker, Integer price, String imageUrl) {
                final ProductUpdateData updateData = ProductUpdateData.builder()
                        .name(name)
                        .maker(maker)
                        .price(price)
                        .imageUrl(imageUrl)
                        .build();

                final Long existsProductId = products.get(0).getId();

                final Product updatedProduct = productService.updateProduct(existsProductId, updateData);

                assertThat(updatedProduct.getId()).isEqualTo(existsProductId);
                assertThat(updatedProduct.getName()).isEqualTo(name);
                assertThat(updatedProduct.getMaker()).isEqualTo(maker);
                assertThat(updatedProduct.getPrice()).isEqualTo(price);
                assertThat(updatedProduct.getImageUrl()).isEqualTo(imageUrl);
            }

            @DisplayName("인자가 유효하지 않을 경우, 예외를 던진다.")
            @ParameterizedTest
            @ArgumentsSource(ProvideInvalidProductArguments.class)
            void updateProductWithInvalidData(String name, String maker, Integer price, String imageUrl) {
                final ProductUpdateData updateData = ProductUpdateData.builder()
                        .name(name)
                        .maker(maker)
                        .price(price)
                        .imageUrl(imageUrl)
                        .build();

                assertThatThrownBy(() -> productService.updateProduct(1000L, updateData))
                        .isInstanceOf(ProductNotFoundException.class)
                        .hasMessage(String.format(ProductNotFoundException.DEFAULT_MESSAGE, 1000L));
            }
        }

        @DisplayName("존재하지 않는 상품의 식별자로 수정하려 하는 경우")
        @Nested
        class Context_with_not_exists_product {
            @DisplayName("예외를 던진다.")
            @ParameterizedTest
            @ValueSource(longs = {Long.MAX_VALUE, Long.MIN_VALUE, 0L, 999L, 1999L, 1199L, 99999L})
            void updateProductWithNotExistsId(Long invalidId) {
                final ProductUpdateData updateData = ProductUpdateData.builder()
                        .maker(PRODUCT_MAKER)
                        .name(PRODUCT_NAME)
                        .price(PRODUCT_PRICE)
                        .imageUrl(PRODUCT_IMAGE_URL)
                        .build();

                assertThatThrownBy(() -> productService.updateProduct(invalidId, updateData))
                        .isInstanceOf(ProductNotFoundException.class)
                        .hasMessage(String.format(ProductNotFoundException.DEFAULT_MESSAGE, invalidId));

            }
        }
    }

    @DisplayName("deleteProduct 메서드는")
    @Nested
    class Describe_deleteProduct {
        @DisplayName("존재하는 상품의 식별자로 삭제를 시도할 경우, 상품이 삭제되고 삭제된 상품 정보가 반환된다.")
        @Test
        void deleteProductWithExistsId() {
            for (Product product : products) {
                final Product deletedProduct = productService.deleteProduct(product.getId());

                assertThat(deletedProduct).isEqualTo(product);
                assertThatThrownBy(() -> productService.findProduct(product.getId()))
                        .isInstanceOf(ProductNotFoundException.class);
            }

        }

        @DisplayName("존재하지 않는 상품의 식별자로 삭제하려 하는 경우, 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(longs = {Long.MAX_VALUE, Long.MIN_VALUE, 0L, 999L, 1999L, 1199L, 99999L})
        void deleteProductWithNotExistsId(Long invalidId) {
            assertThatThrownBy(() -> productService.deleteProduct(invalidId))
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessage(String.format(ProductNotFoundException.DEFAULT_MESSAGE, invalidId));

        }
    }
}
