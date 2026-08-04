package com.sistema.torneos.app.web.model.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.sistema.torneos.app.domain.entity.Gol;
import com.sistema.torneos.app.web.model.GolModel;


@Mapper
public interface GolMapper extends EntityMapper<GolModel, Gol> {

    GolMapper INSTANCE = Mappers.getMapper(GolMapper.class);

    @Override
    @Mapping(source = "partido.id", target = "partido")
    @Mapping(source = "jugador.id", target = "jugador")
    @Mapping(source = "equipoTorneo.id", target = "equipoTorneo")    
    GolModel toModel(Gol entity);

    @Override
    @Mapping(source = "partido", target = "partido.id")
    @Mapping(source = "jugador", target = "jugador.id")
    @Mapping(source = "equipoTorneo", target = "equipoTorneo.id")
    Gol toEntity(GolModel model);
    
}
