package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Categoria;
import com.sistema.torneos.app.domain.repository.CategoriaRepository;
import com.sistema.torneos.app.util.SearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> obtenerCategoriasActivas() {
        return categoriaRepository.findByActivaTrue();
    }

    public Optional<Categoria> obtenerCategoriaById(Long id) {
        return categoriaRepository.findById(id);
    }

    public Optional<Categoria> obtenerCategoriaByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    public Categoria crearCategoria(Categoria categoria) {
        categoria.setActiva(true);
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizarCategoria(Long id, Categoria categoriaActualizada) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if (categoriaOptional.isPresent()) {
            Categoria categoria = categoriaOptional.get();
            if (categoriaActualizada.getNombre() != null) {
                categoria.setNombre(categoriaActualizada.getNombre());
            }
            if (categoriaActualizada.getEdadMinima() != null) {
                categoria.setEdadMinima(categoriaActualizada.getEdadMinima());
            }
            if (categoriaActualizada.getEdadMaxima() != null) {
                categoria.setEdadMaxima(categoriaActualizada.getEdadMaxima());
            }
            if (categoriaActualizada.getDescripcion() != null) {
                categoria.setDescripcion(categoriaActualizada.getDescripcion());
            }
            categoria.setPermitirInscripcion(categoriaActualizada.isPermitirInscripcion());
            categoria.setActiva(categoriaActualizada.isActiva());
            return categoriaRepository.save(categoria);
        }
        return null;
    }

    public void desactivarCategoria(Long id) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if (categoriaOptional.isPresent()) {
            Categoria categoria = categoriaOptional.get();
            categoria.setActiva(false);
            categoriaRepository.save(categoria);
        }
    }

    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }

    public List<Categoria> buscar(String query) {
        List<Categoria> allCategorias = obtenerTodasLasCategorias();
        return SearchUtil.search(allCategorias, query);
    }
}
