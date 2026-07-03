package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.CategoriaTorneo;
import com.sistema.torneos.app.domain.entity.Categoria;
import com.sistema.torneos.app.domain.entity.Torneo;
import com.sistema.torneos.app.domain.repository.CategoriaTorneoRepository;
import com.sistema.torneos.app.domain.repository.CategoriaRepository;
import com.sistema.torneos.app.domain.repository.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaTorneoService {

    @Autowired
    private CategoriaTorneoRepository categoriaTorneoRepository;

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaTorneo> obtenerCategoriasDelTorneo(Long torneoId) {
        Optional<Torneo> torneo = torneoRepository.findById(torneoId);
        if (torneo.isPresent()) {
            return categoriaTorneoRepository.findByTorneo(torneo.get());
        }
        return List.of();
    }

    public List<CategoriaTorneo> obtenerCategoriasActivasDelTorneo(Long torneoId) {
        Optional<Torneo> torneo = torneoRepository.findById(torneoId);
        if (torneo.isPresent()) {
            return categoriaTorneoRepository.findByTorneoAndActivaTrue(torneo.get());
        }
        return List.of();
    }

    public Optional<CategoriaTorneo> obtenerCategoriaTorneoById(Long id) {
        return categoriaTorneoRepository.findById(id);
    }

    public CategoriaTorneo agregarCategoriaAlTorneo(Long torneoId, Long categoriaId, Integer orden) {
        Optional<Torneo> torneo = torneoRepository.findById(torneoId);
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);

        if (torneo.isPresent() && categoria.isPresent()) {
            // Verificar que no esté duplicada
            Optional<CategoriaTorneo> existente = categoriaTorneoRepository
                .findByTorneoIdAndCategoriaId(torneoId, categoriaId);
            
            if (existente.isPresent()) {
                return existente.get();
            }

            CategoriaTorneo categoriaTorneo = new CategoriaTorneo();
            categoriaTorneo.setTorneo(torneo.get());
            categoriaTorneo.setCategoria(categoria.get());
            categoriaTorneo.setActiva(true);
            categoriaTorneo.setOrden(orden);

            return categoriaTorneoRepository.save(categoriaTorneo);
        }
        return null;
    }

    public CategoriaTorneo actualizarCategoriaTorneo(Long id, CategoriaTorneo actualizada) {
        Optional<CategoriaTorneo> categoriaTorneoOptional = categoriaTorneoRepository.findById(id);
        if (categoriaTorneoOptional.isPresent()) {
            CategoriaTorneo categoriaTorneo = categoriaTorneoOptional.get();
            if (actualizada.getOrden() != null) {
                categoriaTorneo.setOrden(actualizada.getOrden());
            }
            categoriaTorneo.setActiva(actualizada.isActiva());
            return categoriaTorneoRepository.save(categoriaTorneo);
        }
        return null;
    }

    public void desactivarCategoriaTorneo(Long id) {
        Optional<CategoriaTorneo> categoriaTorneoOptional = categoriaTorneoRepository.findById(id);
        if (categoriaTorneoOptional.isPresent()) {
            CategoriaTorneo categoriaTorneo = categoriaTorneoOptional.get();
            categoriaTorneo.setActiva(false);
            categoriaTorneoRepository.save(categoriaTorneo);
        }
    }

    public void eliminarCategoriaTorneo(Long id) {
        categoriaTorneoRepository.deleteById(id);
    }
}
