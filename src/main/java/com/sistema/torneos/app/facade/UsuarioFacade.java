package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Usuario;
import com.sistema.torneos.app.domain.repository.UsuarioRepository;
import com.sistema.torneos.app.web.model.UsuarioModel;
import com.sistema.torneos.app.web.model.mapper.ArbitroMapper;
import com.sistema.torneos.app.web.model.mapper.UsuarioMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class UsuarioFacade {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioFacade(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioModel> findAll() {
    	
    	List<Usuario> usuarios = usuarioRepository.findAll();
    	
    	return UsuarioMapper.INSTANCE.toModel(usuarios);
    }

    public UsuarioModel findById(Long id) {
    	 return UsuarioMapper.INSTANCE.toModel(usuarioRepository.findById(id).orElse(null));
    }

    @Transactional
    public UsuarioModel create(UsuarioModel usuarioModel) {
    	
    	Usuario usuario = null;
    	
    	usuario = usuarioRepository.findByNombreAndApellidoAndNombreUsuario(usuario.getNombre(), usuario.getApellido(), usuario.getNombreUsuario());
    	
    	if (usuario!=null) {
    	    throw new RuntimeException("Ya existe un arbitro con estos datos");
    	}
    	
        if (usuarioModel.getPassword() != null && !usuarioModel.getPassword().isBlank()) {
        	usuarioModel.setPassword(passwordEncoder.encode(usuarioModel.getPassword()));
        }
        
        return UsuarioMapper.INSTANCE.toModel(usuarioRepository.save(UsuarioMapper.INSTANCE.toEntity(usuarioModel)));
    }

    @Transactional
    public UsuarioModel update(Long id, UsuarioModel usuario) {
        if (usuarioRepository.existsById(id)) {
            usuario.setId(id);
            if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            return UsuarioMapper.INSTANCE.toModel(usuarioRepository.save(UsuarioMapper.INSTANCE.toEntity(usuario)));
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        }
    }
}
