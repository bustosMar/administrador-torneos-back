package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.Grupo;
import com.sistema.torneos.app.web.model.GrupoModel;

@Mapper
public interface GrupoMapper extends EntityMapper<GrupoModel, Grupo> {

    GrupoMapper INSTANCE = Mappers.getMapper(GrupoMapper.class);

    @Override
    GrupoModel toModel(Grupo entity);

    @Override
    @InheritInverseConfiguration
    Grupo toEntity(GrupoModel model);
    
}
