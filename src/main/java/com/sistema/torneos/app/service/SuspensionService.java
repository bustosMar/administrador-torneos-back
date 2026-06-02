package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Suspension;
import com.sistema.torneos.app.facade.SuspensionFacade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuspensionService {

    private final SuspensionFacade suspensionFacade;

    @Autowired
    public SuspensionService(SuspensionFacade suspensionFacade) {
        this.suspensionFacade = suspensionFacade;
    }

    public List<Suspension> findAll() {
        return suspensionFacade.findAll();
    }

    public Suspension findById(Long id) {
        return suspensionFacade.findById(id);
    }

    public Suspension create(Suspension suspension) {
        return suspensionFacade.create(suspension);
    }

    public Suspension update(Long id, Suspension suspension) {
        return suspensionFacade.update(id, suspension);
    }

    public void delete(Long id) {
        suspensionFacade.delete(id);
    }
}
