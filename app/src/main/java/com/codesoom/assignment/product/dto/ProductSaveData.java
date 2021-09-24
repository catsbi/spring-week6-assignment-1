package com.codesoom.assignment.product.dto;

import com.codesoom.assignment.common.convertors.EntitySupplier;
import com.codesoom.assignment.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaveData implements EntitySupplier<Product> {
    private Long id;

    private String name;

    private String maker;

    private Integer price;

    private String imageUrl;

    @Override
    public Product toEntity() {
        return null;
    }
}
