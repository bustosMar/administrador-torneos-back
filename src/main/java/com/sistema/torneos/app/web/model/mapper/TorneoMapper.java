package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.Torneo;
import com.sistema.torneos.app.web.model.TorneoModel;

@Mapper
public interface TorneoMapper extends EntityMapper<TorneoModel, Torneo> {

    TorneoMapper INSTANCE = Mappers.getMapper(TorneoMapper.class);

    @Override
    @Mapping(source = "municipioEstado.id", target = "municipioEstado")
    @Mapping(source = "municipioEstado.estado.nombre", target = "estadoNombre")
    @Mapping(source = "municipioEstado.municipio.nombre", target = "municipioNombre")
    TorneoModel toModel(Torneo entity);

    @Override
    @InheritInverseConfiguration
    @Mapping(source = "municipioEstado", target = "municipioEstado.id")
    Torneo toEntity(TorneoModel model);
    
}
