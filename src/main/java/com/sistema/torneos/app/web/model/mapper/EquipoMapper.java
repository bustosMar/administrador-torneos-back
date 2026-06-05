package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.Equipo;
import com.sistema.torneos.app.web.model.EquipoModel;

@Mapper
public interface EquipoMapper extends EntityMapper<EquipoModel, Equipo> {

    EquipoMapper INSTANCE = Mappers.getMapper(EquipoMapper.class);

    @Override
    EquipoModel toModel(Equipo entity);

    @Override
    @InheritInverseConfiguration
    Equipo toEntity(EquipoModel model);
    
}
