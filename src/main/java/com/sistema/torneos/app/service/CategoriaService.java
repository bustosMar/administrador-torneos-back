package com.sistema.torneos.app.service;

import com.sistema.torneos.app.facade.CategoriaFacade;
import com.sistema.torneos.app.web.model.CategoriaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaFacade categoriaFacade;

    @Autowired
    public CategoriaService(CategoriaFacade categoriaFacade) {
        this.categoriaFacade = categoriaFacade;
    }

    public List<CategoriaModel> findAll() {
        return categoriaFacade.findAll();
    }

    public List<CategoriaModel> findAllActivas() {
        return categoriaFacade.findAllActivas();
    }

    public CategoriaModel findById(Long id) {
        return categoriaFacade.findById(id);
    }

    public CategoriaModel create(CategoriaModel categoria) {
        return categoriaFacade.create(categoria);
    }

    public CategoriaModel update(Long id, CategoriaModel categoria) {
        return categoriaFacade.update(id, categoria);
    }

    public void delete(Long id) {
        categoriaFacade.delete(id);
    }
}
