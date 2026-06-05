package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.Torneo;
import com.sistema.torneos.app.web.model.TorneoModel;

@Mapper
public interface TorneoMapper extends EntityMapper<TorneoModel, Torneo> {

    TorneoMapper INSTANCE = Mappers.getMapper(TorneoMapper.class);

    @Override
    TorneoModel toModel(Torneo entity);

    @Override
    @InheritInverseConfiguration
    Torneo toEntity(TorneoModel model);
    
}
