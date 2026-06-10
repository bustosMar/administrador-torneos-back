package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Usuario;
import com.sistema.torneos.app.domain.repository.UsuarioRepository;
import com.sistema.torneos.app.domain.entity.Rol;
import com.sistema.torneos.app.domain.repository.RolRepository;
import com.sistema.torneos.app.web.model.UsuarioModel;
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
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioFacade(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
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
    	
    	Usuario usuarioExistente = usuarioRepository
    	        .findByNombreAndApellidoAndNombreUsuario(
    	            usuarioModel.getNombre(),
    	            usuarioModel.getApellido(),
    	            usuarioModel.getNombreUsuario()
    	        )
    	        .orElse(null);

    	    if (usuarioExistente != null) {
    	        throw new RuntimeException("Ya existe un usuario con estos datos");
    	    }

    	  
    	    if (usuarioModel.getPassword() != null && !usuarioModel.getPassword().isBlank()) {
    	        usuarioModel.setPassword(passwordEncoder.encode(usuarioModel.getPassword()));
    	    }

    	    Usuario usuario = UsuarioMapper.INSTANCE.toEntity(usuarioModel);

    	   
    	    if (usuarioModel.getRol() != null) {
    	        Rol rol = rolRepository.findById(usuarioModel.getRol())
    	            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

    	        usuario.getRoles().clear();
    	        usuario.getRoles().add(rol);
    	    }

    	  
    	    Usuario saved = usuarioRepository.save(usuario);

    	    return UsuarioMapper.INSTANCE.toModel(saved);
    }

    @Transactional
    public UsuarioModel update(Long id, UsuarioModel usuario) {

        Usuario entity = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        if (usuario.getNombre() != null) {
            entity.setNombre(usuario.getNombre());
        }

        if (usuario.getApellido() != null) {
            entity.setApellido(usuario.getApellido());
        }

        if (usuario.getNombreUsuario() != null) {
            entity.setNombreUsuario(usuario.getNombreUsuario());
        }

       
        if (usuario.getRol() != null) {
            Rol rol = rolRepository.findById(usuario.getRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            if (!entity.getRoles().contains(rol)) {
                entity.getRoles().add(rol);
            }
        }

        Usuario saved = usuarioRepository.save(entity);

        return UsuarioMapper.INSTANCE.toModel(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        }
    }
}
