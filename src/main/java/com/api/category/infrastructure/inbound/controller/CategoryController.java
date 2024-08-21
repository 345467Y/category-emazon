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
        if(categoryDTO.getName() == null || categoryDTO.getName().isEmpty() || categoryDTO.getName().length()>50){
            throw new IllegalArgumentException("El nombre no puede estar vacío y debe tener un máximo de 50 caracteres.");

        }
        if(categoryDTO.getDescription() == null || categoryDTO.getDescription().isEmpty() || categoryDTO.getDescription().length()>90){
            throw new IllegalArgumentException("La descripción no puede estar vacía y debe tener un máximo de 90 caracteres.");

        }
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("El nombre de la categoría ya está en uso.");
        }

        CategoryEntity category =  categoryMapper.categoryDTOToCategoryEntity(categoryDTO);
        CategoryEntity categoryEntity = this.categoryRepository.save(category);
        return categoryMapper.categoryEntityToCategoryDTO(categoryEntity);
    }

}
