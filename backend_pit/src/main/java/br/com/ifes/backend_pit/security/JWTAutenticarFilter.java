package br.com.ifes.backend_pit.security;

import br.com.ifes.backend_pit.models.usuarios.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@AllArgsConstructor
public class JWTAutenticarFilter extends UsernamePasswordAuthenticationFilter {
    @Value("${app-jwt-secret}")
    private String JWT_SECRET;
    @Value("${app-jwt-expiration-milliseconds}")
    private int JWT_EXPIRATION_MS;

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    usuario.getUsername(),
                    usuario.getPassword(),
                    usuario.getAuthorities()
            ));
        } catch (IOException ex) {
            throw new RuntimeException("Falha ao autenticar usuario");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Usuario usuario = (Usuario) authResult.getPrincipal();

        String token = JWT.create()
                .withSubject(usuario.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .sign(Algorithm.HMAC512(JWT_SECRET));

        response.getWriter().write(token);
        response.getWriter().flush();
    }
}
