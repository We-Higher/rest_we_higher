package com.example.demo.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.member.MemberDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

//인증 완료후 토큰 생성해서 주는 클래스
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
	//토큰 유효시간
	private final Long expiredTime = 1000 * 60L * 60L * 1L; // 유효시간 1시간
	Key key = Keys.hmacShaKeyFor("qwerqwersdfdsdgfsdgsdgfsfgsgafFSGHDFHJGFJGDFHSFGHSGFGDHFGJFGHJGFHJFHSFHDFGJHGJSFHSDHDHJFGHJGFHSFHGSFGHDFfgsdf".getBytes(StandardCharsets.UTF_8));
	private final CustomUserDetailsService userDetailsService;
	
	public String generateJwtToken(MemberDto member) {
		Date now = new Date();
		return Jwts.builder().setSubject(member.getUsername()) // 보통 username
				.setHeader(createHeader()).setClaims(createClaims(member)) // 클레임, 토큰에 포함될 정보
				.setExpiration(new Date(now.getTime() + expiredTime)) // 만료일
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	private Map<String, Object> createHeader() {
		Map<String, Object> header = new HashMap<>();
		header.put("typ", "JWT");
		header.put("alg", "HS256"); // 해시 256 사용하여 암호화
		header.put("regDate", System.currentTimeMillis());
		return header;
	}

	private Map<String, Object> createClaims(MemberDto member) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", member.getUsername()); // username
		//claims.put("roles", member.getType()); // 인가정보
		return claims;
	}

	private Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
	}

	public String getUsernameFromToken(String token) {
		return (String) getClaims(token).get("username");
	}

//	public int getRoleFromToken(String token) {
//		return (int) getClaims(token).get("roles");
//	}

	//request 헤더에 담긴 토큰 꺼냄
	public String resolveToken(HttpServletRequest request) {		
        return request.getHeader("Authorization");
    }
	
	//토큰 유효성 검사
	public boolean validateToken(String token) {
        try {
        	Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
	
	//인증
	public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
