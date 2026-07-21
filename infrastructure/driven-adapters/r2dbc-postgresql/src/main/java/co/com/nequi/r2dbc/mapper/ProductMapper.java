package co.com.nequi.r2dbc.mapper;

import co.com.nequi.model.product.Product;
import co.com.nequi.r2dbc.entity.ProductData;

public class ProductMapper {

    public static Product toModel(ProductData data) {
        return Product.builder()
                .id(data.getId())
                .name(data.getName())
                .stock(data.getStock())
                .branchId(data.getBranchId())
                .build();
    }

    public static ProductData toData(Product product) {
        return ProductData.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();
    }
}
