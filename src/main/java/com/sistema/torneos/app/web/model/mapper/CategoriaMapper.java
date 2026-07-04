package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.Categoria;
import com.sistema.torneos.app.web.model.CategoriaModel;

@Mapper
public interface CategoriaMapper extends EntityMapper<CategoriaModel, Categoria> {

    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);

    @Override
    CategoriaModel toModel(Categoria entity);

    @Override
    @InheritInverseConfiguration
    Categoria toEntity(CategoriaModel model);
    
}
