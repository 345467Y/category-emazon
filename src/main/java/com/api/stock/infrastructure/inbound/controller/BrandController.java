package com.api.stock.infrastructure.inbound.controller;

import com.api.stock.domain.model.BrandDTO;
import com.api.stock.domain.model.entity.BrandEntity;
import com.api.stock.domain.repository.BrandRepository;
import com.api.stock.mapper.BrandMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    public BrandController(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @GetMapping
    public ResponseEntity<List<BrandDTO>> getBrands() {
        return ResponseEntity.ok(brandRepository
                .findAll()
                .stream()
                .map(brandMapper::brandEntityToBrandDTO)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable Long id) {
        Optional<BrandEntity> brandOptional = brandRepository
                .findById(id);
        return brandOptional.map(brandEntity -> ResponseEntity.ok().body(brandMapper.brandEntityToBrandDTO(brandEntity)))
                .orElseThrow(() -> new NoSuchElementException("No se encontró la entidad solicitada"));
    }


    @PostMapping
    public ResponseEntity<BrandDTO> saveBrand(@RequestBody BrandDTO brandDTO) {
        if(brandDTO.getName() == null || brandDTO.getName().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre no puede estar vacío.");

        }
        if(brandDTO.getName().length()>50){
            throw new IllegalArgumentException("El nombre debe tener un máximo de 50 caracteres.");
        }
        if(brandDTO.getDescription() == null || brandDTO.getDescription().trim().isEmpty()){
            throw new IllegalArgumentException("La descripción no puede estar vacía.");

        }
        if(brandDTO.getDescription().length()>120){
            throw new IllegalArgumentException("La descripción debe tener un máximo de 120 caracteres.");
        }
        List<BrandEntity> brands = brandRepository.findAll();
        for (BrandEntity brand: brands) {
            if(brandDTO.getName().trim().equals(brand.getName().trim()) && (brandDTO.getId() == null || !brandDTO.getId().equals(brand.getId()))){
                throw new IllegalArgumentException("El nombre de la marca ya está en uso.");
            }
        }

        BrandEntity brand =  brandMapper.brandDTOToBrandEntity(brandDTO);
        BrandEntity brandEntity = this.brandRepository.save(brand);
        return ResponseEntity.ok(brandMapper.brandEntityToBrandDTO(brandEntity));
    }

}
