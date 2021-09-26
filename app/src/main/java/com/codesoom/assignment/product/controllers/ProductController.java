package com.codesoom.assignment.product.controllers;

import com.codesoom.assignment.product.application.ProductService;
import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.domain.ProductList;
import com.codesoom.assignment.product.dto.ProductSaveData;
import com.codesoom.assignment.product.dto.ProductUpdateData;
import com.codesoom.assignment.product.dto.ProductViewData;
import com.codesoom.assignment.product.errors.InvalidProductArgumentException;
import com.codesoom.assignment.product.errors.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 상품에 대한 HTTP Request 요청을 처리한다.
 */
@RestController
@RequestMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * 모든 상품 목록을 조회한다.
     *
     * @return 상품 목록
     */
    @GetMapping
    public List<ProductViewData> findAllProduct() {
        final ProductList foundProducts = productService.findAllProduct();

        if (foundProducts.isEmpty()) {
            return Collections.emptyList();
        }
        return foundProducts.convert(ProductViewData::from);
    }

    /**
     * 상품 상세정보를 조회한다.
     *
     * @param id 상품 식별자
     * @return 상품 상세정보
     * @throws ProductNotFoundException 상품 정보를 찾을 수 없는 경우
     * @throws NumberFormatException    식별자 타입이 유효하지 않은 경우
     */
    @GetMapping("{id}")
    public ProductViewData findProduct(@PathVariable long id) {
        final Product foundProduct = productService.findProduct(id);

        return ProductViewData.from(foundProduct);
    }

    /**
     * 상품을 저장한다.
     *
     * @param saveData 저장하려는 상품 정보
     * @return 저장된 상품 정보
     * @throws InvalidProductArgumentException 저장하려는 상품 정보가 유효하지 않은 경우
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductViewData createProduct(@RequestBody ProductSaveData saveData) {
        final Product savedProduct = productService.createProduct(saveData);

        return ProductViewData.from(savedProduct);
    }

    /**
     * 상품 정보를 수정한다.
     *
     * @param id         수정하려는 상품 식별자
     * @param updateData 수정할 상품 정보
     * @return 수정된 상품 정보
     * @throws NumberFormatException           상품 식별자가 유효하지 않은 타입일 경우
     * @throws ProductNotFoundException        식별자로 상품을 찾을 수 없는 경우
     * @throws InvalidProductArgumentException 수정할 상품 정보가 유효하지 않을 경우
     */
    @RequestMapping(path = "{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ProductViewData updateProduct(@PathVariable long id, @RequestBody ProductUpdateData updateData) {
        final Product updatedProduct = productService.updateProduct(id, updateData);

        return ProductViewData.from(updatedProduct);
    }

    /**
     * 상품을 삭제한다.
     *
     * @param id 삭제 할 상품 식별자
     * @throws ProductNotFoundException 식별자로 상품을 찾을 수 없는 경우
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
    }
}
