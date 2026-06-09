package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.web.model.EquipoEnTorneoModel;

@Mapper
public interface EquipoEnTorneoMapper extends EntityMapper<EquipoEnTorneoModel, EquipoEnTorneo> {

    EquipoEnTorneoMapper INSTANCE = Mappers.getMapper(EquipoEnTorneoMapper.class);

    @Override
    @Mapping(source = "equipo.id", target = "equipo")
    @Mapping(source = "equipo.nombre", target = "equipoNombre")

    @Mapping(source = "torneo.id", target = "torneo")
    @Mapping(source = "torneo.nombre", target = "torneoNombre")

    @Mapping(source = "grupo.id", target = "grupo")
    @Mapping(source = "grupo.nombre", target = "grupoNombre")
    EquipoEnTorneoModel toModel(EquipoEnTorneo entity);
    
    @Override
    @InheritInverseConfiguration
    @Mapping(source = "equipo", target = "equipo.id")
    @Mapping(source = "torneo", target = "torneo.id")
    @Mapping(source = "grupo", target = "grupo.id")
    EquipoEnTorneo toEntity(EquipoEnTorneoModel model);
    
}
