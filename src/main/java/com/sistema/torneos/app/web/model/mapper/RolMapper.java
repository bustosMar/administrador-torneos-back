package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.sistema.torneos.app.domain.entity.Rol;
import com.sistema.torneos.app.web.model.RolModel;


@Mapper
public interface RolMapper extends EntityMapper<RolModel, Rol> {

    RolMapper INSTANCE = Mappers.getMapper(RolMapper.class);

    @Override
    RolModel toModel(Rol entity);

    @Override
    @InheritInverseConfiguration
    Rol toEntity(RolModel model);
    
}
