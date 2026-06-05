package com.sistema.torneos.app.web.model.mapper;

import java.util.List;

public interface EntityMapper<M, E> {
    
    public M toModel(E entity);
    public E toEntity(M model);

    public List<M> toModel(List<E> entities);
    public List<E> toEntity(List<M> models);
    
}
