package com.api.stock.infrastructure.inbound.controller;

import com.api.stock.domain.model.ProductDTO;
import com.api.stock.domain.model.entity.ProductEntity;
import com.api.stock.domain.repository.ProductRepository;
import com.api.stock.mapper.ProductMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductController(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @GetMapping
    private ResponseEntity<List<ProductDTO>> getProducts() {
        return ResponseEntity.ok(productRepository
                .findAll()
                .stream()
                .map(productEntity -> productMapper.productEntityToProductDTO(productEntity))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    private ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Optional<ProductEntity> productOptional = productRepository
                .findById(id);
        return productOptional.map(productEntity -> ResponseEntity.ok().body(productMapper.productEntityToProductDTO(productEntity)))
                .orElseThrow(() -> new NoSuchElementException("No se encontró la entidad solicitada"));
    }


    @PostMapping
    private ResponseEntity<ProductDTO> saveProduct(@RequestBody ProductDTO productDTO) {
        ProductEntity productEntity = this.productRepository.save(
                productMapper.productDTOToProductEntity(productDTO)
        );
        return ResponseEntity.ok(productMapper.productEntityToProductDTO(productEntity));
    }

}
