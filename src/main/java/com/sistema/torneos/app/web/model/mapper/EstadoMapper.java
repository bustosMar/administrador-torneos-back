package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.Estado;
import com.sistema.torneos.app.web.model.EstadoModel;

@Mapper
public interface EstadoMapper extends EntityMapper<EstadoModel, Estado> {

    EstadoMapper INSTANCE = Mappers.getMapper(EstadoMapper.class);

}
