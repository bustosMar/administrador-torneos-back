package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Usuario;

import java.util.List;

public interface UsuarioService {
    List<Usuario> findAll();
    Usuario findById(Long id);
    Usuario findByUsuario(String usuario);
    Usuario create(Usuario usuario);
    Usuario update(Long id, Usuario usuario);
    void delete(Long id);
}
