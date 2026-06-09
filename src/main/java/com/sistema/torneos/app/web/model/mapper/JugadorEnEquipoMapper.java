package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.sistema.torneos.app.domain.entity.JugadorEnEquipo;
import com.sistema.torneos.app.web.model.JugadorEnEquipoModel;

@Mapper
public interface JugadorEnEquipoMapper extends EntityMapper<JugadorEnEquipoModel, JugadorEnEquipo> {

    JugadorEnEquipoMapper INSTANCE = Mappers.getMapper(JugadorEnEquipoMapper.class);

    @Override
    @Mapping(source = "jugador.id", target = "jugador")
    @Mapping(source = "jugador.nombre", target = "jugadorNombre")
    @Mapping(source = "jugador.apellido", target = "jugadorApellido")

    @Mapping(source = "equipo.id", target = "equipo")
    @Mapping(source = "equipo.nombre", target = "equipoNombre")
    

    @Mapping(source = "torneo.id", target = "torneo")
    @Mapping(source = "torneo.nombre", target = "torneoNombre")
    JugadorEnEquipoModel toModel(JugadorEnEquipo entity);

    @Override
    @InheritInverseConfiguration
    @Mapping(source = "jugador", target = "jugador.id")
    @Mapping(source = "equipo", target = "equipo.id")
    @Mapping(source = "torneo", target = "torneo.id")
    JugadorEnEquipo toEntity(JugadorEnEquipoModel model);
}