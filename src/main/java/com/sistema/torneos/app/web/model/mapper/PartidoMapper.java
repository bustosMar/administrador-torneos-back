package com.sistema.torneos.app.web.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.web.model.PartidoModel;

@Mapper
public interface PartidoMapper extends EntityMapper<PartidoModel, Partido> {

    PartidoMapper INSTANCE = Mappers.getMapper(PartidoMapper.class);

    @Override
    @Mapping(source = "grupo", target = "grupo.id")
    @Mapping(source = "arbitro", target = "arbitro.id")
    @Mapping(source = "jornada", target = "jornada.id")
    @Mapping(source = "equipoLocal", target = "equipoLocal.id")
    @Mapping(source = "equipoVisitante", target = "equipoVisitante.id")
    @Mapping(target = "goles", ignore = true)
    @Mapping(target = "presencias", ignore = true)
    Partido toEntity(PartidoModel model);

    @Override
    @Mapping(source = "grupo.id", target = "grupo")
    @Mapping(source = "jornada.id", target = "jornada")
    @Mapping(source = "jornada.numeroJornada", target = "numeroJornada")
    @Mapping(source = "equipoLocal.id", target = "equipoLocal")
    @Mapping(source = "equipoLocal.equipo.nombre", target = "equipoLocalNombre")
    @Mapping(source = "equipoVisitante.id", target = "equipoVisitante")
    @Mapping(source = "equipoVisitante.equipo.nombre", target = "equipoVisitanteNombre")
    @Mapping(target = "grupoNombre", expression = "java(entity.getGrupo() != null ? entity.getGrupo().getNombre() : null)")
    @Mapping(target = "arbitro", expression = "java(entity.getArbitro() != null ? entity.getArbitro().getId() : null)")
    @Mapping(target = "arbitroNombre", expression = "java(entity.getArbitro() != null ? entity.getArbitro().getNombre() : null)")
    PartidoModel toModel(Partido entity);

    List<Partido> toEntityList(List<PartidoModel> modelList);

    List<PartidoModel> toModelList(List<Partido> entityList);
}