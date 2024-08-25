package com.api.stock.mapper;

import com.api.stock.domain.model.CategoryDTO;
import com.api.stock.domain.model.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryEntity categoryDTOToCategoryEntity(CategoryDTO categoryDTO);

    CategoryDTO categoryEntityToCategoryDTO(CategoryEntity categoryEntity);

}
