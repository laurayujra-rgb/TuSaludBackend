package bo.com.edu.diplomado.tuSaliud.Auth.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.AccountsEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    @Value("${spring.jwt.expiration}")
    private long jwtExpiration;

    @Value("${spring.jwt.refresh-expiration}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails, AccountsEntity accountsEntity) {
        Map<String, Object> claims = new HashMap<>();
        // Agregar roles al token
        claims.put("email", userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities().stream()

                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        claims.put("personId", accountsEntity.getPerson().getPersonId());
        claims.put("name", accountsEntity.getPerson().getPersonName());
        claims.put("lastName", accountsEntity.getPerson().getPersonFatherSurname());

        claims.put("state", accountsEntity.getPerson().getPersonStatus());

        return buildToken(claims, userDetails, jwtExpiration);
    }

    public Long extractPersonId(String token){
        return extractClaim(token, claims -> claims.get("personId", Long.class));
    }

    public String extractPersonName(String token) {
        return extractClaim(token, claims -> claims.get("personName", String.class));
    }

    public String extractPersonSurname(String token) {
        return extractClaim(token, claims -> claims.get("personSurname", String.class));
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    public String generateRefreshToken(UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())

                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}