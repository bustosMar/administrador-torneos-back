package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.sistema.torneos.app.domain.entity.Sancion;
import com.sistema.torneos.app.web.model.SancionModel;


@Mapper
public interface SancionMapper extends EntityMapper<SancionModel, Sancion> {

    SancionMapper INSTANCE = Mappers.getMapper(SancionMapper.class);

    @Override
    @Mapping(source = "partido.id", target = "partido")
    @Mapping(source = "jugador.id", target = "jugador")
    @Mapping(source = "equipoTorneo.id", target = "equipoTorneo")    
    SancionModel toModel(Sancion entity);

    @Override
    @Mapping(source = "partido", target = "partido.id")
    @Mapping(source = "jugador", target = "jugador.id")
    @Mapping(source = "equipoTorneo", target = "equipoTorneo.id")
    Sancion toEntity(SancionModel model);
    
}
