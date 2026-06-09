package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.web.model.response.EquipoEnTorneoResponse;

@Mapper
public interface EquipoEnTorneoResponseMapper extends EntityMapper<EquipoEnTorneoResponse, EquipoEnTorneo> {

    EquipoEnTorneoResponseMapper INSTANCE = Mappers.getMapper(EquipoEnTorneoResponseMapper.class);

    @Override
    @Mapping(source = "equipo.nombre", target = "equipo")
    @Mapping(source = "torneo.nombre", target = "torneo")
    @Mapping(source = "grupo.nombre", target = "grupo")
    EquipoEnTorneoResponse toModel(EquipoEnTorneo entity);

    @Override
    @InheritInverseConfiguration
    @Mapping(source = "equipo", target = "equipo.nombre")
    @Mapping(source = "torneo", target = "torneo.nombre")
    @Mapping(source = "grupo", target = "grupo.nombre")
    EquipoEnTorneo toEntity(EquipoEnTorneoResponse model);   
}
