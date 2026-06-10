package com.sistema.torneos.app.web.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.torneos.app.service.VigenciaService;
import com.sistema.torneos.app.web.model.request.LoginRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import static com.sistema.torneos.config.TokenJwtConfig.*;

import jakarta.validation.Valid;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private VigenciaService vigenciaService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) throws JsonProcessingException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage())));
        }

        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getNombreUsuario(), loginRequest.getPassword()));
                      
            vigenciaService.obtenerVigenciaActual(loginRequest.getClave());
            
      
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(Map.of(
                    "message", "Usuario, contraseña o clave incorrectos.",
                    "error", ex.getMessage()
            ));
        }

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        String username = user.getUsername();
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
        boolean isAdmin = roles.stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        Claims claims = Jwts.claims()
                .add("authorities", new ObjectMapper().writeValueAsString(roles))
                .add("username", username)
                .add("isAdmin", isAdmin)
                .build();

        Date expirationDate = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30);
        String jwt = Jwts.builder()
                .subject(username)
                .claims(claims)
                .signWith(SECRET_KEY)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .compact();

        Map<String, Object> body = new HashMap<>();
        body.put("token", jwt);
        body.put("username", username);
        body.put("expiresAt", expirationDate.getTime());
        body.put("roles", roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        body.put("message", String.format("Hola %s has iniciado sesión con éxito", username));

        return ResponseEntity.ok(body);
    }
}
