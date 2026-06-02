package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Usuario;
import com.sistema.torneos.app.facade.UsuarioFacade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioFacade usuarioFacade;

    @Autowired
    public UsuarioService(UsuarioFacade usuarioFacade) {
        this.usuarioFacade = usuarioFacade;
    }

    public List<Usuario> findAll() {
        return usuarioFacade.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioFacade.findById(id);
    }

    public Usuario create(Usuario usuario) {
        return usuarioFacade.create(usuario);
    }

    public Usuario update(Long id, Usuario usuario) {
        return usuarioFacade.update(id, usuario);
    }

    public void delete(Long id) {
        usuarioFacade.delete(id);
    }
}
