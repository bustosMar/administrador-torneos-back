package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.web.model.JugadorModel;

@Mapper
public interface JugadorMapper extends EntityMapper<JugadorModel, Jugador> {

    JugadorMapper INSTANCE = Mappers.getMapper(JugadorMapper.class);

    @Override
    JugadorModel toModel(Jugador entity);

    @Override
    @InheritInverseConfiguration
    Jugador toEntity(JugadorModel model);
    
}
