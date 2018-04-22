package com.zaghir.project.jwtSpringSecurity.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaghir.project.jwtSpringSecurity.entites.AppUser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import scala.annotation.meta.setter;

public class JWTAuthentificationFilter extends UsernamePasswordAuthenticationFilter {

	
	private AuthenticationManager authenticationManager;
	
//	@Autowired
//	private ObjectMapper objectMapper;
	
	public JWTAuthentificationFilter(AuthenticationManager authenticationManager) { 
		this.authenticationManager = authenticationManager;
	}
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		AppUser appUser =null ;
		/** quand les données sont envoyés sous format www-url-encoded
		 * on recupere les données avec l'objet request sous la forme 
		 * String username=request.getParameter("username"); 
		 */
		 
		/** au lieu de faire un new pour serialiser avec jackson on cree un bean et on l'inject avec spring */
		try {
			appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println("****************");
		System.out.println("username:"+appUser.getUsername());
		System.out.println("password:"+appUser.getPassword());
		return authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword()));
	}
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		/** une fois authentifié spring security passe vers cette methode successfulAuthentication */
		User springUser =(User)authResult.getPrincipal();
		/** on ajoute les claims au token*/
		String jwt = Jwts.builder()
				.setSubject(springUser.getUsername())
				.setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
				.claim("roles", springUser.getAuthorities())
				.compact(); // compact c'est l'encodage Bash64Url par defaut
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX +jwt);
				
	}
}
