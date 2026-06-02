package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Suspension;
import com.sistema.torneos.app.domain.repository.SuspensionRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class SuspensionFacade {

    private final SuspensionRepository suspensionRepository;

    @Autowired
    public SuspensionFacade(SuspensionRepository suspensionRepository) {
        this.suspensionRepository = suspensionRepository;
    }

    public List<Suspension> findAll() {
        return suspensionRepository.findAll();
    }

    public Suspension findById(Long id) {
        return suspensionRepository.findById(id).orElse(null);
    }

    @Transactional
    public Suspension create(Suspension suspension) {
        return suspensionRepository.save(suspension);
    }

    @Transactional
    public Suspension update(Long id, Suspension suspension) {
        if (suspensionRepository.existsById(id)) {
            suspension.setId(id);
            return suspensionRepository.save(suspension);
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (suspensionRepository.existsById(id)) {
            suspensionRepository.deleteById(id);
        }
    }
}
