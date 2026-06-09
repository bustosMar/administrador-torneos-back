package com.sistema.torneos.app.service;

import com.sistema.torneos.app.facade.UsuarioFacade;
import com.sistema.torneos.app.web.model.UsuarioModel;

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

    public List<UsuarioModel> findAll() {
        return usuarioFacade.findAll();
    }

    public UsuarioModel findById(Long id) {
        return usuarioFacade.findById(id);
    }

    public UsuarioModel create(UsuarioModel usuario) {
        return usuarioFacade.create(usuario);
    }

    public UsuarioModel update(Long id, UsuarioModel usuario) {
        return usuarioFacade.update(id, usuario);
    }

    public void delete(Long id) {
        usuarioFacade.delete(id);
    }
}
