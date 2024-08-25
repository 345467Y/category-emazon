package com.api.stock.mapper;

import com.api.stock.domain.model.ProductDTO;
import com.api.stock.domain.model.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductEntity productDTOToProductEntity(ProductDTO product);

    ProductDTO productEntityToProductDTO(ProductEntity productEntity);

}
