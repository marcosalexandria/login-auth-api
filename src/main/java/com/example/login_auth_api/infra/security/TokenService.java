package com.example.login_auth_api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.login_auth_api.domain.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String chavePrivada;

    //Gera token
    public String gerarToken (Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(chavePrivada);

            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(this.geraDataExpiracaoToken())
                    .sign(algorithm);

            return token;
        }catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao autenticar");
        }
    }

    //verifica se o token eh verdadeiro
    public String validaToken(String token){
        try {

            Algorithm algorithm = Algorithm.HMAC256(chavePrivada);

            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException exception){
            return null;
        }
    }

    //Gera Hora "2" de expiracao
    private Instant geraDataExpiracaoToken(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
