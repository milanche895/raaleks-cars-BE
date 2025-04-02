package com.project.brka_media.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.brka_media.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JwtUtil {

	private String secret;
	private long jwtExpirationInMs;

	@Value("${jwt.secret}")
	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Value("${jwt.expirationDateInMs}")
	public void setJwtExpirationInMs(long jwtExpirationInMs) {
		this.jwtExpirationInMs = jwtExpirationInMs;
	}

	public String generateToken(UserDetails userDetails, UserEntity userEntity) {
		Map<String, Object> claims = new HashMap<>();
		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
		if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			claims.put("isAdmin", true);
		}
		if (roles.contains(new SimpleGrantedAuthority("ROLE_CLIENT"))) {
			claims.put("isClient", true);
		}
		claims.put("id", userEntity.getId() );
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date((long) (System.currentTimeMillis() + jwtExpirationInMs)))
				.signWith(SignatureAlgorithm.HS512, secret).compact();

	}

	public boolean validateToken(String authToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		} catch (ExpiredJwtException ex) {
			throw ex;
		}
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claims.getSubject();

	}
	public Long getIdFromToken(String token) {
		String jwtToken = token.substring(7, token.length());
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken).getBody();
		Long id = claims.get("id", Long.class);
		return id;

	}
	public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

		List<SimpleGrantedAuthority> roles = null;

		Boolean isAdmin = claims.get("isAdmin", Boolean.class);
		Boolean isClient = claims.get("isClient", Boolean.class);
		if (isAdmin != null && isAdmin) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		if (isClient != null && isClient) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"));
		}
		return roles;
	}
	public Boolean checkByClientIdOrAdmin(String token, Long clientId) {
		if (null == token) {

			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED!");
		}
		String jwtToken = token.substring(7, token.length());
		final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken).getBody();
		Integer userId = (Integer)claims.get("id");
		Boolean isAdmin = claims.get("isAdmin", Boolean.class);
		if ((isAdmin != null && isAdmin) || userId.equals(clientId.intValue())){
			return true;
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized!");
		}
	}
}