package com.sistema.torneos.config;

import com.sistema.torneos.app.domain.entity.Rol;
import com.sistema.torneos.app.domain.entity.Usuario;
import com.sistema.torneos.app.domain.repository.RolRepository;
import com.sistema.torneos.app.domain.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RolRepository roleRepository,
                           UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Rol adminRole = roleRepository.findByNombre("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Rol(null, "ROLE_ADMIN")));
        Rol userRole = roleRepository.findByNombre("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Rol(null, "ROLE_USER")));
        Rol refereeRole = roleRepository.findByNombre("ROLE_REFEREE")
                .orElseGet(() -> roleRepository.save(new Rol(null, "ROLE_REFEREE")));

        if (usuarioRepository.findByNombreUsuario("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellido("System");
            admin.setNombreUsuario("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(List.of(adminRole, userRole));
            usuarioRepository.save(admin);
        }

        if (usuarioRepository.findByNombreUsuario("Usuario").isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setNombre("suario");
            usuario.setApellido("Usuario");
            usuario.setNombreUsuario("usuario");
            usuario.setPassword(passwordEncoder.encode("usuario123"));
            usuario.setRoles(List.of(userRole));
            usuarioRepository.save(usuario);
        }

        if (usuarioRepository.findByNombreUsuario("Arbitro").isEmpty()) {
            Usuario arbitro = new Usuario();
            arbitro.setNombre("Arbitro");
            arbitro.setApellido("Arbitro");
            arbitro.setNombreUsuario("arbitro");
            arbitro.setPassword(passwordEncoder.encode("arbitro123"));
            arbitro.setRoles(List.of(refereeRole));
            usuarioRepository.save(arbitro);
        }
    }
}
