package com.sistema.torneos.app.web.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.sistema.torneos.app.domain.entity.Municipio;
import com.sistema.torneos.app.web.model.MunicipioModel;

@Mapper
public interface MunicipioMapper extends EntityMapper<MunicipioModel, Municipio> {

    MunicipioMapper INSTANCE = Mappers.getMapper(MunicipioMapper.class);

}
