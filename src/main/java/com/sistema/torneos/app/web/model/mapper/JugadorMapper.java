package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.web.model.JugadorModel;

@Mapper
public interface JugadorMapper extends EntityMapper<JugadorModel, Jugador> {

    JugadorMapper INSTANCE = Mappers.getMapper(JugadorMapper.class);

    @Override
    @Mapping(source = "equipo.id", target = "equipo")
    JugadorModel toModel(Jugador entity);

    @Override
    @InheritInverseConfiguration
    @Mapping(source = "equipo", target = "equipo.id")
    Jugador toEntity(JugadorModel model);
    
}
