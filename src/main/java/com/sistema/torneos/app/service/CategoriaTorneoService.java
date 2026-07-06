package com.sistema.torneos.app.service;

import com.sistema.torneos.app.facade.CategoriaTorneoFacade;
import com.sistema.torneos.app.web.model.CategoriaTorneoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaTorneoService {

    private final CategoriaTorneoFacade categoriaTorneoFacade;

    @Autowired
    public CategoriaTorneoService(CategoriaTorneoFacade categoriaTorneoFacade) {
        this.categoriaTorneoFacade = categoriaTorneoFacade;
    }

    public List<CategoriaTorneoModel> findAll() {
        return categoriaTorneoFacade.findAll();
    }
  
    public List<CategoriaTorneoModel> findByTorneoId(Long torneoId) {
        return categoriaTorneoFacade.findByTorneoId(torneoId);
    }

    public CategoriaTorneoModel findById(Long id) {
        return categoriaTorneoFacade.findById(id);
    }

    public CategoriaTorneoModel create(CategoriaTorneoModel categoriaTorneo) {
        return categoriaTorneoFacade.create(categoriaTorneo);
    }

    public CategoriaTorneoModel update(Long id, CategoriaTorneoModel categoriaTorneo) {
        return categoriaTorneoFacade.update(id, categoriaTorneo);
    }

    public void delete(Long id) {
        categoriaTorneoFacade.delete(id);
    }
}
