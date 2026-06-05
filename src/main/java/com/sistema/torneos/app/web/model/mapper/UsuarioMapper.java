package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.Usuario;
import com.sistema.torneos.app.web.model.UsuarioModel;

@Mapper
public interface UsuarioMapper extends EntityMapper<UsuarioModel, Usuario> {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Override
    UsuarioModel toModel(Usuario entity);

    @Override
    @InheritInverseConfiguration
    Usuario toEntity(UsuarioModel model);
    
}
