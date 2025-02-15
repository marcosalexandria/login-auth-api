package com.example.login_auth_api.infra.security;
import com.example.login_auth_api.domain.entity.Usuario;
import com.example.login_auth_api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = this.repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario Não encontrado."));
        return new org.springframework.security.core.userdetails.User(usuario.getEmail(), usuario.getSenha(), new ArrayList<>());
    }
}
