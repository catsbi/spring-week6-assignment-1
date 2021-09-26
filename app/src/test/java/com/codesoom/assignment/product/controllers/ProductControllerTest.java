package com.codesoom.assignment.product.controllers;

import com.codesoom.assignment.product.application.ProductService;
import com.codesoom.assignment.product.config.WebTestConfig;
import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.domain.ProductRepository;
import com.codesoom.assignment.product.dto.ProductSaveData;
import com.codesoom.assignment.product.dto.ProductUpdateData;
import com.codesoom.assignment.product.errors.ProductNotFoundException;
import com.codesoom.assignment.product.provider.ProvideInvalidProductArguments;
import com.codesoom.assignment.product.provider.ProvideValidProductArguments;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ProductController 클래스")
@WebMvcTest({ProductController.class})
@Import(WebTestConfig.class)
@AutoConfigureMockMvc
class ProductControllerTest {

    public static final String PRODUCT_API = "/products";

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private List<Product> products;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        products = productRepository.saveAll(Arrays.asList(츄르, 스크래쳐, 펫모닝, 두더지잡기));
    }

    @DisplayName("GET /products API는")
    @Nested
    class Describe_get_products {
        @DisplayName("저장된 상품 목록이 있을 경우")
        @Nested
        class Context_with_exists_products {
            @DisplayName("200 상태 코드와, 상품 목록을 반환한다.")
            @Test
            void getFulfilledProducts() throws Exception {
                mockMvc.perform(get(PRODUCT_API).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(products.size())));
            }
        }

        @DisplayName("저장된 상품 목록이 없을 경우")
        @Nested
        class Context_without_exists_products {
            @BeforeEach
            void prepareGetProducts() {
                productRepository.deleteAll();
            }

            @DisplayName("200 상태 코드와, 빈 목록을 반환한다.")
            @Test
            void getEmptyProducts() throws Exception {
                mockMvc.perform(get(PRODUCT_API).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"));
            }
        }
    }

    @DisplayName("GET /products/{id} API는")
    @Nested
    class Describe_get_products_id {
        @DisplayName("존재하는 상품의 식별자로 조회할 경우, 200상태코드와 상품 상세정보를 반환한다.")
        @Test
        void getProductWithExistsId() throws Exception {
            for (Product product : products) {
                mockMvc.perform(
                                get(PRODUCT_API + "/" + product.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value(product.getName()))
                        .andExpect(jsonPath("$.maker").value(product.getMaker()))
                        .andExpect(jsonPath("$.price").value(product.getPrice()))
                        .andExpect(jsonPath("$.imageUrl").value(product.getImageUrl()));
            }
        }

        @DisplayName("존재하지 않는 상품의 식별자로 조회할 경우, 404 상태코드와 에러 메세지를 반환한다.")
        @ParameterizedTest
        @ValueSource(longs = {Long.MAX_VALUE, Long.MIN_VALUE, 0L, 999L, 1999L, 1199L, 99999L})
        void getProductWithNotExistsId(Long invalidId) throws Exception {
            mockMvc.perform(
                            get(PRODUCT_API + "/" + invalidId)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message")
                            .value(String.format(ProductNotFoundException.DEFAULT_MESSAGE, invalidId)));
        }

        @DisplayName("잘못 된 타입의 식별자로 조회할 경우, 400 상태코드와 에러 메세지를 반환한다.")
        @ParameterizedTest
        @ValueSource(strings = {"NaN", "Undefined", "null", "Null", "NULL"})
        void getProductWithInvalidId(String invalidId) throws Exception {
            mockMvc.perform(
                            get(PRODUCT_API + "/" + invalidId)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message")
                            .value("For input string: \"" + invalidId + "\""));
        }
    }


    @DisplayName("POST /products API는")
    @Nested
    class Describe_post_product {
        @DisplayName("유효한 제품 정보를 제공할 경우, 201 상태코드와 등록된 제품 상세정보를 반환한다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideValidProductArguments.class)
        void postProductWithValidData(String name, String maker, Integer price, String imageUrl) throws Exception {
            final ProductSaveData saveData = makeProductSaveData(name, maker, price, imageUrl);
            final String contentBody = objectMapper.writeValueAsString(saveData);

            mockMvc.perform(
                            post(PRODUCT_API)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .content(contentBody))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value(name))
                    .andExpect(jsonPath("$.maker").value(maker))
                    .andExpect(jsonPath("$.price").value(price))
                    .andExpect(jsonPath("$.imageUrl").value(imageUrl));

        }

        @DisplayName("유효하지 않은 제품 정보를 제공할 경우, 400 상태코드와 에러 메세지를 반환한다.")
        @ParameterizedTest
        @ArgumentsSource(ProvideInvalidProductArguments.class)
        void postProductWithInvalidData(String name, String maker, Integer price, String imageUrl) throws Exception {
            final ProductSaveData saveData = makeProductSaveData(name, maker, price, imageUrl);
            final String contentBody = objectMapper.writeValueAsString(saveData);

            mockMvc.perform(
                            post(PRODUCT_API)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .content(contentBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());

        }

        private ProductSaveData makeProductSaveData(String name, String maker, Integer price, String imageUrl) {
            return ProductSaveData.builder()
                    .name(name)
                    .maker(maker)
                    .price(price)
                    .imageUrl(imageUrl)
                    .build();
        }
    }


    @DisplayName("PATCH /products/{id} API는")
    @Nested
    class Describe_patch_products_id {
        @DisplayName("등록된 상품의 정보를 수정하려고 하는 경우")
        @Nested
        class Context_with_exists_product {
            @DisplayName("수정하려는 정보가 유효하면, 200 상태코드와 수정된 상품 상세정보를 반환한다.")
            @ParameterizedTest
            @ArgumentsSource(ProvideValidProductArguments.class)
            void updateProductWithValidData(String name, String maker, Integer price, String imageUrl) throws Exception {
                final ProductUpdateData productUpdateData = makeProductUpdateData(name, maker, price, imageUrl);
                final String contentBody = objectMapper.writeValueAsString(productUpdateData);
                final Product originProduct = products.get(0);

                mockMvc.perform(
                                put(PRODUCT_API + "/" + originProduct.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .content(contentBody))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(originProduct.getId()))
                        .andExpect(jsonPath("$.name").value(name))
                        .andExpect(jsonPath("$.maker").value(maker))
                        .andExpect(jsonPath("$.price").value(price))
                        .andExpect(jsonPath("$.imageUrl").value(imageUrl));

            }

            @DisplayName("수정하려는 정보가 유효하지 않으면, 400 상태코드와 에러 메세지를 반환한다.")
            @ParameterizedTest
            @ArgumentsSource(ProvideInvalidProductArguments.class)
            void updateProductWithInvalidData(String name, String maker, Integer price, String imageUrl) throws Exception {
                final ProductUpdateData productUpdateData = makeProductUpdateData(name, maker, price, imageUrl);
                final String contentBody = objectMapper.writeValueAsString(productUpdateData);
                final Product originProduct = products.get(0);

                mockMvc.perform(
                                put(PRODUCT_API + "/" + originProduct.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .content(contentBody))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").exists());
            }
        }

        @DisplayName("등록되지 않은 상품의 정보를 수정하려고 하는 경우")
        @Nested
        class Context_with_not_exists_product {
            @DisplayName("404 상태코드와 에러 메세지를 반환한다.")
            @ParameterizedTest
            @ValueSource(longs = {Long.MAX_VALUE, Long.MIN_VALUE, 0L, 999L, 1999L, 1199L, 99999L})
            void updateProductWithNotExistsId(Long invalidId) throws Exception {
                final ProductUpdateData productUpdateData
                        = makeProductUpdateData(PRODUCT_NAME, PRODUCT_MAKER, PRODUCT_PRICE, PRODUCT_IMAGE_URL);
                final String contentBody = objectMapper.writeValueAsString(productUpdateData);

                mockMvc.perform(
                                put(PRODUCT_API + "/" + invalidId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .content(contentBody))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message")
                                .value(String.format(ProductNotFoundException.DEFAULT_MESSAGE, invalidId)));

            }
        }

        @DisplayName("수정하려는 상품의 식별자 타입이 유효하지 않을 경우, 400 상태코드와 에러 메세지를 반환한다.")
        @ParameterizedTest
        @ValueSource(strings = {"NaN", "Undefined", "null", "Null", "NULL"})
        void updateProductWithInvalidId(String invalidId) throws Exception {
            final ProductUpdateData productUpdateData
                    = makeProductUpdateData(PRODUCT_NAME, PRODUCT_MAKER, PRODUCT_PRICE, PRODUCT_IMAGE_URL);
            final String contentBody = objectMapper.writeValueAsString(productUpdateData);

            mockMvc.perform(
                            put(PRODUCT_API + "/" + invalidId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .content(contentBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message")
                            .value("For input string: \"" + invalidId + "\""));
        }

        private ProductUpdateData makeProductUpdateData(String name, String maker, Integer price, String imageUrl) {
            return ProductUpdateData.builder()
                    .name(name)
                    .maker(maker)
                    .price(price)
                    .imageUrl(imageUrl)
                    .build();
        }
    }

    @DisplayName("DELETE /products/{id} API는")
    @Nested
    class Describe_delete_products_id {
        @DisplayName("존재하는 상품의 식별자로 삭제를 하려는 경우, 204 상태코드가 반환되며 정상적으로 삭제된다.")
        @Test
        void deleteWithExistsId() throws Exception {
            for (Product product : products) {
                mockMvc.perform(
                                delete(PRODUCT_API + "/" + product.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isNoContent())
                        .andExpect(jsonPath("$").doesNotExist());
            }
        }

        @DisplayName("존재하지 않는 상품의 식별자로 삭제를 하려는 경우, 404 상태코드와 에러 메세지를 반환한다.")
        @ParameterizedTest
        @ValueSource(longs = {Long.MAX_VALUE, Long.MIN_VALUE, 0L, 999L, 1999L, 1199L, 99999L})
        void deleteWithNotExistsId(Long invalidId) throws Exception {
            mockMvc.perform(
                            delete(PRODUCT_API + "/" + invalidId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message")
                            .value(String.format(ProductNotFoundException.DEFAULT_MESSAGE, invalidId)));
        }

        @DisplayName("유효하지 않은 타입의 식별자로 삭제를 하려 하면, 400 상태코드와 에러 메세지를 반환한다.")
        @ParameterizedTest
        @ValueSource(strings = {"NaN", "Undefined", "null", "Null", "NULL"})
        void deleteWithInvalidId(String invalidId) throws Exception {
            mockMvc.perform(
                            delete(PRODUCT_API + "/" + invalidId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message")
                            .value("For input string: \"" + invalidId + "\""));
        }
    }
}
