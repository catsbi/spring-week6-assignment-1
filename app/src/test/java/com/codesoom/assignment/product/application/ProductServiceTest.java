package com.codesoom.assignment.product.application;

import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static com.codesoom.assignment.product.ConstantsForProductTest.두더지잡기;
import static com.codesoom.assignment.product.ConstantsForProductTest.스크래쳐;
import static com.codesoom.assignment.product.ConstantsForProductTest.츄르;
import static com.codesoom.assignment.product.ConstantsForProductTest.펫모닝;

@DisplayName("ProductService 클래스")
@DataJpaTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    private ProductService productService;

    private List<Product> products;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);

        products = productRepository.saveAll(Arrays.asList(츄르, 스크래쳐, 펫모닝, 두더지잡기));
    }


    @DisplayName("findAllProduct 메서드는")
    @Nested
    class Describe_findAllProduct {
        @DisplayName("등록된 상품이 있을 경우 상품 목록을 반환한다.")
        @Test
        void findAllWithRegisteredProducts() {

        }

        @DisplayName("등록된 상품이 없을 경우 빈 목록을 반환한다.")
        @Test
        void findAllWithoutRegisteredProducts() {

        }
    }

    @DisplayName("findProduct 메서드는")
    @Nested
    class Describe_findProduct {
        @DisplayName("존재하는 상품의 식별자를 조회하면, 상품 상세정보를 반환한다.")
        @Test
        void findProductWithExistsId() {

        }

        @DisplayName("존재하지 않는 상품의 식별자를 조회하면, 예외를 던진다.")
        @Test
        void findProductWithNotExistsId() {

        }
    }

    @DisplayName("createProduct 메서드는")
    @Nested
    class Describe_createProduct {
        @DisplayName("인자가 유효할 경우, 상품을 등록한 뒤 반환한다.")
        @Test
        void createProductWithValidData() {

        }

        @DisplayName("인자가 유효하지 않을 경우, 예외를 던진다.")
        @Test
        void createProductWithInvalidData() {

        }
    }

    @DisplayName("updateProduct 메서드는")
    @Nested
    class Describe_updateProduct {
        @DisplayName("존재하는 상품의 식별자로 수정하려 하는 경우")
        @Nested
        class Context_with_exists_product {
            @DisplayName("인자가 유효할 경우, 상품을 수정한 뒤 반환한다.")
            @Test
            void updateProductWithValidData() {

            }

            @DisplayName("인자가 유효하지 않을 경우, 예외를 던진다.")
            @Test
            void updateProductWithInvalidData() {

            }
        }

        @DisplayName("존재하지 않는 상품의 식별자로 수정하려 하는 경우")
        @Nested
        class Context_with_not_exists_product {
            @DisplayName("예외를 던진다.")
            @Test
            void updateProductWithNotExistsId() {

            }
        }
    }

    @DisplayName("deleteProduct 메서드는")
    @Nested
    class Describe_deleteProduct {
        @DisplayName("존재하는 상품의 식별자로 삭제를 시도할 경우, 상품이 삭제되고 삭제된 상품 정보가 반환된다.")
        @Test
        void deleteProductWithExistsId() {

        }

        @DisplayName("존재하지 않는 상품의 식별자로 삭제하려 하는 경우, 예외를 던진다.")
        @Test
        void deleteProductWithNotExistsId() {

        }
    }
}
