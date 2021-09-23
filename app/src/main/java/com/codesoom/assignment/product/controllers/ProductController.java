package com.codesoom.assignment.product.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * TODO 구현해야 할 기능
 * 고양이 장난감 목록 얻기    -GET /products
 * 고양이 장난감 상세 조회하기 -GET /products/{id}
 * 고양이 장난감 등록하기     -POST /products
 * 고양이 장난감 수정하기     -PATCH /products/{id}
 * 고양이 장난감 삭제하기     -DELETE /products/{id}
 */

/**
 * 상품에 대한 HTTP Request 요청을 처리한다.
 */
@RestController
@RequestMapping(value = "products", consumes = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

}
