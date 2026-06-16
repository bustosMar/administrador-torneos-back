package com.sistema.torneos.app.web.model.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
import org.mapstruct.factory.Mappers;

import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.web.model.request.PartidoRequest;

@Mapper
public interface PartidoMapper extends EntityMapper<PartidoRequest, Partido> {

    PartidoMapper INSTANCE = Mappers.getMapper(PartidoMapper.class);
    
    @Override
    @Mapping(source = "idTorneo", target = "torneo.id")
    @Mapping(source = "idGrupo", target = "grupo.id")
    @Mapping(source = "idLocal", target = "equipoLocal.id")
    @Mapping(source = "idVisitante", target = "equipoVisitante.id")
    Partido toEntity(PartidoRequest request);
    
    List<Partido> toEntityList(List<PartidoRequest> requestList);

    
    
    
}
