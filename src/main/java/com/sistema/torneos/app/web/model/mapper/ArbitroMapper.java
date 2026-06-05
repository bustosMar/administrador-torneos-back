package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.Arbitro;
import com.sistema.torneos.app.web.model.ArbitroModel;

@Mapper
public interface ArbitroMapper extends EntityMapper<ArbitroModel, Arbitro> {

    ArbitroMapper INSTANCE = Mappers.getMapper(ArbitroMapper.class);

    @Override
    ArbitroModel toModel(Arbitro entity);

    @Override
    @InheritInverseConfiguration
    Arbitro toEntity(ArbitroModel model);
    
}
