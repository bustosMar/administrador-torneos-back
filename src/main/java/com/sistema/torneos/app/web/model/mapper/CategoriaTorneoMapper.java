package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.sistema.torneos.app.domain.entity.CategoriaTorneo;
import com.sistema.torneos.app.web.model.CategoriaTorneoModel;

@Mapper
public interface CategoriaTorneoMapper extends EntityMapper<CategoriaTorneoModel, CategoriaTorneo> {

    CategoriaTorneoMapper INSTANCE = Mappers.getMapper(CategoriaTorneoMapper.class);

    @Override
    @Mapping(source = "torneo.id", target = "torneo")
    @Mapping(source = "torneo.nombre", target = "torneoNombre")
    @Mapping(source = "categoria.id", target = "categoria")
    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    CategoriaTorneoModel toModel(CategoriaTorneo entity);

    @Override
    @InheritInverseConfiguration
    @Mapping(source = "torneo", target = "torneo.id")
    @Mapping(source = "categoria", target = "categoria.id")
    CategoriaTorneo toEntity(CategoriaTorneoModel model);
    
}
