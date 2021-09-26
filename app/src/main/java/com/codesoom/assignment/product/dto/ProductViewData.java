package com.codesoom.assignment.product.dto;

import com.codesoom.assignment.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductViewData {
    private Long id;

    private String name;

    private String maker;

    private Integer price;

    private String imageUrl;

    public static ProductViewData from(Product product) {
        return ProductViewData.builder()
                .id(product.getId())
                .name(product.getName())
                .maker(product.getMaker())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
    }
}
