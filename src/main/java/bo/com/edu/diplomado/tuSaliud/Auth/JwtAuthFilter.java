package bo.com.edu.diplomado.tuSaliud.Auth;

import bo.com.edu.diplomado.tuSaliud.Auth.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Extraer información adicional
                Long personId = jwtService.extractPersonId(jwt);
                String personName = jwtService.extractPersonName(jwt);
                String personSurname = jwtService.extractPersonSurname(jwt);

                // Extraer roles
                List<String> roles = jwtService.extractRoles(jwt);
                Collection<? extends GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Crear autenticación con información adicional
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(
                        userDetails,
                        null,
                        authorities,
                        personId,
                        personName,
                        personSurname
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    public static class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
        private final Long personId;
        private final String personName;
        private final String personSurname;

        public JwtAuthenticationToken(Object principal, Object credentials,
                                      Collection<? extends GrantedAuthority> authorities,
                                      Long personId, String personName, String personSurname) {
            super(principal, credentials, authorities);
            this.personId = personId;
            this.personName = personName;
            this.personSurname = personSurname;
        }

        public Long getPersonId() { return personId; }
        public String getPersonName() { return personName; }
        public String getPersonSurname() { return personSurname; }
    }
}
