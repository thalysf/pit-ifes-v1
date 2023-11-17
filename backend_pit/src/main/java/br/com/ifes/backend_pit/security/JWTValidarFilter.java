package br.com.ifes.backend_pit.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTValidarFilter extends OncePerRequestFilter {
    public static final String HEADER_ATRIBUTO = "Authorization";
    public static final String ATRIBUTO_PREFIXO = "Bearer ";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = getTokenFromRequest(request);

        try{
            //valida token
            if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){

                // get username from token
                String username = jwtTokenProvider.getUsernameFromJWT(token);

                // load the user associated with token
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            chain.doFilter(request, response);
        } catch (Exception ex){
            chain.doFilter(request, response);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request){

        String bearerToken = request.getHeader(HEADER_ATRIBUTO);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(ATRIBUTO_PREFIXO)){
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }
}
