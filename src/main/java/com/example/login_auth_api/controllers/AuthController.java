package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.entity.Usuario;
import com.example.login_auth_api.dto.LoginRequestDTO;
import com.example.login_auth_api.dto.LoginResponseDTO;
import com.example.login_auth_api.dto.RegisterRequestDTO;
import com.example.login_auth_api.infra.security.TokenService;
import com.example.login_auth_api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity Login (@RequestBody LoginRequestDTO body){
        Usuario usuario = repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("Usuario não encontrado."));
        if(passwordEncoder.matches(body.senha(), usuario.getSenha())){
            String token = this.tokenService.gerarToken(usuario);
            return ResponseEntity.ok(new LoginResponseDTO(usuario.getNome(), token));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        Optional<Usuario> usuario = this.repository.findByEmail(body.email());

        if(usuario.isEmpty()) {
            Usuario newUser = new Usuario();
            newUser.setSenha(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setNome(body.nome());
            this.repository.save(newUser);

            String token = this.tokenService.gerarToken(newUser);
            return ResponseEntity.ok(new LoginResponseDTO(newUser.getNome(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
