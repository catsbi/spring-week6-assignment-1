package com.codesoom.assignment.product.domain;

import com.codesoom.assignment.product.provider.ProvideValidProductArguments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.codesoom.assignment.product.ConstantsForProductTest.두더지잡기;
import static com.codesoom.assignment.product.ConstantsForProductTest.스크래쳐;
import static com.codesoom.assignment.product.ConstantsForProductTest.츄르;
import static com.codesoom.assignment.product.ConstantsForProductTest.펫모닝;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("ProductRepository 클래스")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void cleanUp() {
        productRepository.deleteAll();
    }

    @DisplayName("findAll 메서드는")
    @Nested
    class Describe_findAll {
        @DisplayName("저장된 상품(들)이 있을 경우")
        @Nested
        class Context_with_registered_products {
            private List<Product> products;

            @BeforeEach
            void prepareFindAllTest() {
                products = productRepository.saveAll(Arrays.asList(츄르, 펫모닝, 스크래쳐, 두더지잡기));
            }

            @DisplayName("저장된 상품 목록을 반환한다.")
            @Test
            void findAllWithFulfilledProducts() {
                final List<Product> foundProducts = productRepository.findAll();

                assertThat(foundProducts.size()).isEqualTo(products.size());
                assertThat(foundProducts).containsAll(products);
            }
        }

        @DisplayName("저장된 상품(들)이 없을 경우")
        @Nested
        class Context_with_empty {
            @BeforeEach
            void prepareFindAllTest() {
                productRepository.deleteAll();
            }

            @DisplayName("빈 상품 목록을 반환한다.")
            @Test
            void findAllWithEmptyProducts() {
                final List<Product> foundProducts = productRepository.findAll();

                assertThat(foundProducts).isEmpty();
            }

        }
    }

    @DisplayName("findById 메서드는")
    @Nested
    class Describe_findById {
        private Product product;

        @BeforeEach
        void setUp() {
            product = productRepository.save(츄르);
        }

        @DisplayName("존재하는 상품의 식별자를 조회하려 하면, 상품 상세정보를 반환한다.")
        @Test
        void findByIdWithExistsProduct() {
            final Optional<Product> foundProductOpt = productRepository.findById(product.getId());

            assertThat(foundProductOpt)
                    .isPresent()
                    .contains(product);
        }

        @DisplayName("존재하지 않는 상품의 식별자를 조회하려 하면, Optional.empty를 반환한다.")
        @ParameterizedTest
        @ValueSource(longs = {Long.MIN_VALUE, Long.MAX_VALUE, 0L, 99L, 999L, 5000L, 9999L, 123456L})
        void findByIdWithNotExistsProduct(Long id) {
            final Optional<Product> foundProductOpt = productRepository.findById(id);

            assertThat(foundProductOpt).isEmpty();
        }
    }

    @DisplayName("save 메서드는")
    @Nested
    class Describe_save {
        @DisplayName("저장하려는 상품 식별자가 이미 존재하는 경우")
        @Nested
        class Context_with_existed_product {
            private Product originProduct;

            @BeforeEach
            void prepareSave() {
                final Product mockProduct = Product.builder()
                        .name("테스트")
                        .maker("테스트회사")
                        .price(10000)
                        .imageUrl("http://localhost:8080/test")
                        .build();

                originProduct = productRepository.save(mockProduct);
            }


            @DisplayName("등록된 상품 정보에 덮어쓰기로 저장된다.")
            @ParameterizedTest
            @ArgumentsSource(ProvideValidProductArguments.class)
            void saveProductWithExistedProduct(String name, String maker, Integer price, String imageUrl) {
                final Product newProduct = Product.builder()
                        .id(originProduct.getId())
                        .name(name)
                        .maker(maker)
                        .price(price)
                        .imageUrl(imageUrl)
                        .build();


                final Product savedProduct = productRepository.save(newProduct);

                assertThat(productRepository.findAll()).hasSize(1);
                assertThat(savedProduct.getId()).isEqualTo(originProduct.getId());
                assertThat(savedProduct.getName()).isEqualTo(name);
                assertThat(savedProduct.getMaker()).isEqualTo(maker);
                assertThat(savedProduct.getPrice()).isEqualTo(price);
                assertThat(savedProduct.getImageUrl()).isEqualTo(imageUrl);

            }
        }

        @DisplayName("저장하려는 상품 식별자가 등록되지 않았을 경우")
        @Nested
        class Context_with_not_existed_product {

            @DisplayName("새로운 상품으로 등록하고 등록한 상품을 반환한다.")
            @ParameterizedTest
            @ArgumentsSource(ProvideValidProductArguments.class)
            void saveWithNotRegisteredProduct(String name, String maker, Integer price, String imageUrl) {
                final Product newProduct = Product.builder()
                        .name(name)
                        .maker(maker)
                        .price(price)
                        .imageUrl(imageUrl)
                        .build();

                final Product savedProduct = productRepository.save(newProduct);

                assertThat(productRepository.findAll()).hasSize(1);
                assertThat(savedProduct.getName()).isEqualTo(name);
                assertThat(savedProduct.getMaker()).isEqualTo(maker);
                assertThat(savedProduct.getPrice()).isEqualTo(price);
                assertThat(savedProduct.getImageUrl()).isEqualTo(imageUrl);
            }
        }
    }

    @DisplayName("saveAll 메서드는")
    @Nested
    class Describe_saveAll {
        private List<Product> productList;
        private Set<Product> productSet;

        @BeforeEach
        void setUp() {
            productList = Arrays.asList(츄르, 펫모닝);
            productSet = new HashSet<>(Arrays.asList(스크래쳐, 두더지잡기));
        }

        @DisplayName("상품 목록을 전체 저장 한다.")
        @Test
        void saveAllProducts() {
            final List<Product> savedProducts1 = productRepository.saveAll(productList);
            final List<Product> savedProducts2 = productRepository.saveAll(productSet);

            assertThat(savedProducts1).hasSize(productList.size());
            assertThat(savedProducts2).hasSize(productSet.size());

            final List<Product> products = productRepository.findAll();
            assertThat(products).hasSize(productList.size() + productSet.size());

        }

    }

    @DisplayName("deleteAll 메서드는")
    @Nested
    class Describe_deleteAll {


        @DisplayName("상품 목록이 존재 할 경우")
        @Nested
        class Context_with_exists_product_list {
            @BeforeEach
            void setUp() {
                productRepository.saveAll(Arrays.asList(츄르, 스크래쳐, 펫모닝, 두더지잡기));
            }

            @DisplayName("모든 상품 목록을 제거한다.")
            @Test
            void deleteAllProduct() {
                productRepository.deleteAll();

                assertThat(productRepository.findAll()).isEmpty();
            }
        }

        @DisplayName("상품 목록이 없을 경우")
        @Nested
        class Context_with_not_exists_product_list {

            @DisplayName("예외가 발생하지 않는다.")
            @RepeatedTest(50)
            void deleteAllWithoutProductList() {
                assertThatCode(() -> productRepository.deleteAll())
                        .doesNotThrowAnyException();

            }
        }
    }

    @DisplayName("delete 메서드는")
    @Nested
    class Describe_delete {
        private Product registeredProduct;
        private Product unRegisteredProduct;

        @BeforeEach
        void setUp() {
            registeredProduct = productRepository.save(츄르);
            unRegisteredProduct = 스크래쳐;
        }


        @DisplayName("존재하는 상품을 삭제하려 하면, 정상적으로 삭제된다.")
        @Test
        void deleteWithExistsProduct() {
            productRepository.delete(registeredProduct);

            final Optional<Product> foundProductOpt = productRepository.findById(registeredProduct.getId());

            assertThat(foundProductOpt).isEmpty();
        }

        @DisplayName("존재하지 않는 상품을 삭제하려 해도, 예외가 발생하지 않는다.")
        @RepeatedTest(50)
        void deleteWithNotExistsProduct() {
            assertThatCode(() -> productRepository.delete(unRegisteredProduct))
                    .doesNotThrowAnyException();
        }
    }
}
