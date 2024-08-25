package com.api.stock.mapper;

import com.api.stock.domain.model.BrandDTO;
import com.api.stock.domain.model.entity.BrandEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandEntity brandDTOToBrandEntity(BrandDTO brandDTO);

    BrandDTO brandEntityToBrandDTO(BrandEntity brandEntity);

}
