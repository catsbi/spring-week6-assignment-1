package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 상품과 관련된 HTTP 요청을 처리합니다.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin
public class ProductController {
    private final ProductService productService;
    private final AuthenticationService authenticationService;

    /**
     * 모든 상품을 반환합니다.
     *
     * @return 모든 상품의 리스트
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * id에 해당하는 상품을 반환합니다.
     *
     * @param id 조회할 상품의 id
     * @return 조회한 상품
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 상품을 저장하고 저장된 상품을 반환합니다.
     *
     * @param authorization 인가 정보
     * @param productData   저장할 상품의 정보
     * @return 저장된 상품
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ProductData productData
    ) {
        authenticationService.parseToken(getSubstring(authorization));
        return productService.createProduct(productData);
    }

    /**
     * id에 해당하는 상품을 수정하고 수정된 상품을 반환합니다.
     *
     * @param authorization 인가 정보
     * @param id            수정할 상품의 id
     * @param productData   수정할 상품의 정보
     * @return 수정된 상품
     */
    @PatchMapping("{id}")
    public Product update(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        authenticationService.parseToken(getSubstring(authorization));
        return productService.updateProduct(id, productData);
    }

    /**
     * id에 해당하는 상품을 삭제합니다.
     *
     * @param authorization 인가 정보
     * @param id            삭제할 상품의 id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        authenticationService.parseToken(getSubstring(authorization));
        productService.deleteProduct(id);
    }

    private String getSubstring(@RequestHeader("Authorization") String authorization) {
        return authorization.substring("Bearer ".length());
    }
}
