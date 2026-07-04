package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Categoria;
import com.sistema.torneos.app.domain.repository.CategoriaRepository;
import com.sistema.torneos.app.web.model.CategoriaModel;
import com.sistema.torneos.app.web.model.mapper.CategoriaMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class CategoriaFacade {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaFacade(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaModel> findAll() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return CategoriaMapper.INSTANCE.toModel(categorias);
    }

    public List<CategoriaModel> findAllActivas() {
        List<Categoria> categorias = categoriaRepository.findByActivaTrue();
        return CategoriaMapper.INSTANCE.toModel(categorias);
    }

    public CategoriaModel findById(Long id) {
        return CategoriaMapper.INSTANCE.toModel(
                categoriaRepository.findById(id).orElse(null)
        );
    }

    @Transactional
    public CategoriaModel create(CategoriaModel categoriaModel) {
        Categoria categoria = CategoriaMapper.INSTANCE.toEntity(categoriaModel);
        categoria.setActiva(true);
        return CategoriaMapper.INSTANCE.toModel(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaModel update(Long id, CategoriaModel categoriaModel) {
        if (categoriaRepository.existsById(id)) {
            Categoria entity = CategoriaMapper.INSTANCE.toEntity(categoriaModel);
            entity.setId(id);
            return CategoriaMapper.INSTANCE.toModel(categoriaRepository.save(entity));
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
        }
    }
}

