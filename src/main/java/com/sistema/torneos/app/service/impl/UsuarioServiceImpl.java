package com.sistema.torneos.app.service.impl;

import com.sistema.torneos.app.domain.entity.Usuario;
import com.sistema.torneos.app.domain.repository.UsuarioRepository;

import org.springframework.transaction.annotation.Transactional;
import com.sistema.torneos.app.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;
    
    
    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return (List<Usuario>) this.repository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Usuario create(Usuario usuario) {
        return this.repository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario update(Long id, Usuario usuario) {
        Usuario existingUsuario = this.findById(id);
        if (existingUsuario != null) {
            // Update the properties of existingUsuario with those from usuario
            return this.repository.save(existingUsuario);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public Usuario findByUsuario(String usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsuario'");
    }

   
}
