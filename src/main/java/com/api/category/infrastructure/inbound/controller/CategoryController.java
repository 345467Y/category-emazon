package com.api.category.infrastructure.inbound.controller;

import com.api.category.domain.model.CategoryDTO;
import com.api.category.domain.model.entity.CategoryEntity;
import com.api.category.mapper.CategoryMapper;
import com.api.category.domain.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    private List<CategoryDTO> getCategories() {
        return categoryRepository
                .findAll()
                .stream()
                .map(categoryEntity -> categoryMapper.categoryEntityToCategoryDTO(categoryEntity))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    private CategoryDTO getCategoryById(@PathVariable Long id) {
        Optional<CategoryEntity> categoryOptional = categoryRepository
                .findById(id);
        if(categoryOptional.isPresent()){
            return categoryOptional.map(categoryEntity -> categoryMapper.categoryEntityToCategoryDTO(categoryEntity)).get();
        }
        return null;
    }


    @PostMapping
    private CategoryDTO saveCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = this.categoryRepository.save(
                categoryMapper.categoryDTOToCategoryEntity(categoryDTO)
        );
        return categoryMapper.categoryEntityToCategoryDTO(categoryEntity);
    }

}
