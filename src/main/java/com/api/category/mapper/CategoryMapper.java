package com.api.category.mapper;

import com.api.category.domain.model.CategoryDTO;
import com.api.category.domain.model.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryEntity categoryDTOToCategoryEntity(CategoryDTO categoryDTO);

    CategoryDTO categoryEntityToCategoryDTO(CategoryEntity categoryEntity);

}
