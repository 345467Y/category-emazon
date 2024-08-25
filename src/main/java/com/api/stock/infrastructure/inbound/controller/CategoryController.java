package com.api.stock.infrastructure.inbound.controller;

import com.api.stock.domain.model.CategoryDTO;
import com.api.stock.domain.model.entity.CategoryEntity;
import com.api.stock.mapper.CategoryMapper;
import com.api.stock.domain.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public ResponseEntity<Page<CategoryDTO>> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CategoryDTO> categoryDTOPage = categoryRepository.findAll(pageable)
                .map(categoryMapper::categoryEntityToCategoryDTO);
        return  ResponseEntity.ok(categoryDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<CategoryEntity> categoryOptional = categoryRepository
                .findById(id);
        return categoryOptional.map(categoryEntity -> ResponseEntity.ok().body(categoryMapper.categoryEntityToCategoryDTO(categoryEntity)))
                .orElseThrow(() -> new NoSuchElementException("No se encontró la entidad solicitada"));
    }


    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        if(categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre no puede estar vacío.");

        }
        if(categoryDTO.getName().length()>50){
            throw new IllegalArgumentException("El nombre debe tener un máximo de 50 caracteres.");
        }
        if(categoryDTO.getDescription() == null || categoryDTO.getDescription().trim().isEmpty()){
            throw new IllegalArgumentException("La descripción no puede estar vacía.");

        }
        if(categoryDTO.getDescription().length()>90){
            throw new IllegalArgumentException("La descripción debe tener un máximo de 90 caracteres.");
        }
        List<CategoryEntity> categories = categoryRepository.findAll();
        for (CategoryEntity category: categories) {
            if(categoryDTO.getName().trim().equals(category.getName().trim()) && (categoryDTO.getId() == null || !categoryDTO.getId().equals(category.getId()))){
                throw new IllegalArgumentException("El nombre de la categoría ya está en uso.");
            }
        }

        CategoryEntity category =  categoryMapper.categoryDTOToCategoryEntity(categoryDTO);
        CategoryEntity categoryEntity = this.categoryRepository.save(category);
        return ResponseEntity.ok(categoryMapper.categoryEntityToCategoryDTO(categoryEntity));
    }

}
