package com.ibm.srap.services.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ibm.srap.client_beans.DefaultResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author MihaiMesesan
 *
 * JWT token related
 */
@Component
public class SecurityUtil implements Serializable {

	private static final long serialVersionUID = -3301605591108950415L;

	private static final String CLAIM_KEY_USERNAME = "sub";
	private static final String CLAIM_KEY_CREATED = "created";
	private static final String CLAIM_KEY_CNUM = "cnum";
	private static final String CLAIM_KEY_MANAGER = "manager";

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;
	
	public ResponseEntity<DefaultResponse> getForbiddenResponse() {
		DefaultResponse response = new DefaultResponse();
		response.setMessage("Please provide valid manager or delegate credentials");
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	public String getUsernameFromToken(String token) {
		final Claims claims = getClaimsFromToken(token);
		if (claims == null) return null;
		return claims.getSubject();
	}
	
	public String getCnumFromToken(String token) {
		final Claims claims = getClaimsFromToken(token);
		if (claims == null) return null;
		return (String) claims.get(CLAIM_KEY_CNUM);
	}
	
	public Integer getManagerFromToken(String token) {
		final Claims claims = getClaimsFromToken(token);
		if (claims == null) return null;
		return (Integer) claims.get(CLAIM_KEY_MANAGER);
	}

	public Date getCreatedDateFromToken(String token) {
		final Claims claims = getClaimsFromToken(token);
		if (claims == null) return null;
		return new Date((Long) claims.get(CLAIM_KEY_CREATED));
	}

	public Date getExpirationDateFromToken(String token) {
		final Claims claims = getClaimsFromToken(token);
		if (claims == null) return null;
		return claims.getExpiration();
	}

	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	private Boolean isTokenExpired(String token) {
		final Date tokenExpiration = getExpirationDateFromToken(token);
		return tokenExpiration.before(new Date());
	}

	public String generateToken(String userMail) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, userMail);
		claims.put(CLAIM_KEY_CREATED, new Date());
		return generateToken(claims);
	}

	private String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token) {
		return !isTokenExpired(token);
	}

	public String refreshToken(String token) {
		final Claims claims = getClaimsFromToken(token);
		if (claims == null) return null;
		claims.put(CLAIM_KEY_CREATED, new Date());
		return generateToken(claims);
	}

	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}
}
