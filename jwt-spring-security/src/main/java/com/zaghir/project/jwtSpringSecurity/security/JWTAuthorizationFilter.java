package com.zaghir.project.jwtSpringSecurity.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		/** pour resoudre le probleme de cors Cross origin Resource Sharring
		 * il faut ajouter des params dans le reponse que le filtr envoi
		 * le probleme c'est du au navigateur ne interdit de recuperer les resources d'un domaine
		 * et envoyer les autres requetes vers un autre domaine si non il faut avoir les droits coté serveur
		 */
		
		/** j'autorise tous les domaine , mais c'est possible de definir
		 * les domaine que j'autorise on remplaçant * par les domaine
		 */
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		/**
		 * autorisé angular de recupere les entete , car le jeton est dans la proprité authorisation
		 */
		response.addHeader("Acces-Control-Allow-Headres", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, authorization");
		/**
		 * pour exposer les entetes au front depuis le  
		 */
		response.addHeader("Access-Control-Expose-Headers","Access-Control-Allow-Origin, Access-Control-Allow-Credentials, authorization");
		
		/**
		 * angular envoi une requet avec Option du verbe HTTP
		 * Option permet au client Http d'interoger le serveur pour fournir les options de communication à utiliser pour une ressource cibléé
		 * ou un ensemblre de ressources (Méthodes autorisées, Entetes autorisées, Origines autorisés,..) utilisé souvent comme requete de pre-vérification Cross-Origin
		 * le test if() suivant permet de dire a spring scurity n'est pas la pein de faire les filtre de secutity sur la Methode ==> OPTION si non spring la block
		 * le serveur envoi a angular tous les droit qui a besoin apres une demande avec OPTION
		 * mais toutes les requetes POST , GET ,DELETE , UPDATE envoi les entetes    
		 */
		if(request.getMethod().equals("OPTIONS")){
			response.setStatus(HttpServletResponse.SC_OK);
		}
		
		
		
		String jwtToken = request.getHeader(SecurityConstants.HEADER_STRING);
		System.out.println("********** doFilterInternal");
		System.out.println(jwtToken);
		if(jwtToken ==null || !jwtToken.startsWith(SecurityConstants.TOKEN_PREFIX)){
			/** si il y a pas de jeton dans le header ou il ne commence pas par le PREFIX
			 * on pass au vers spring security la main car on a pas un Jwt
			 */
			filterChain.doFilter(request, response);
			return ;
		}
		/** si non on traite notre Jeton*/
		Claims claims = Jwts.parser()
				.setSigningKey(SecurityConstants.SECRET)  // on signe 
				.parseClaimsJws(jwtToken.replace(SecurityConstants.TOKEN_PREFIX, "")) // on suprime la chaine Bearer
				.getBody();
		String username= claims.getSubject();
		ArrayList<Map<String,String>> roles = (ArrayList<Map<String, String>>) claims.get("roles");
		/** spring security a besoin des roles sous format GrantedAuthority
		 * on recuper les roles de type Map< cle=>'authority' , value => 'ADMIN'>
		 * est spring security ajoute les authorities dans le constext de l application 
		 */
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		roles.forEach(r->{
			authorities.add(new SimpleGrantedAuthority(r.get("authority"))) ;
		});
		
		UsernamePasswordAuthenticationToken authenticatedUser = 
				new UsernamePasswordAuthenticationToken(username ,null , authorities) ; // le null c'est pour dire que le password  on a pas besoin n a
		
		/** charger les urilisateur authentifié dans le context de spring*/
		SecurityContextHolder
			.getContext()
				.setAuthentication(authenticatedUser);
		/** dire a filter chaine de passer */
		filterChain.doFilter(request, response);
		
	}
	

}
