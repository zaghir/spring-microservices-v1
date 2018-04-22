package com.zaghir.project.jwtSpringSecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService ;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Bean
	public ObjectMapper getObjectMapper(){
		return new ObjectMapper() ;
	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/**
		 * 1 - permiere methode d'authentification c'est en memory 
		 */
		/*auth.inMemoryAuthentication()
			.withUser("admin").password("1234").roles("ADMIN","USER")
			.and()
			.withUser("user").password("1234").roles("USER");*/
		/**
		 *  2 - deuxieme methode par l'utilisation d une requete pour verifier le user ,
		 *      et une autre pour recupere les role de user 
		 *      c'est une authentification basic aussi
		 */
		/*auth.jdbcAuthentication().usersByUsernameQuery("query pour verifier le user").authoritiesByUsernameQuery("query pour recupere les roles");*/
		auth
			.userDetailsService(userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder);
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		/**
		 *  faille de csrf
		 *  qui recupere le session id de la cookie et envoie la donnée
		 *  le champ hiden dans la page loggin avec le jeton que sprinf security cree n'est plus ajouté 
		 *  on desactive le token syncrones Token pour utiliser le Jwt  
		 *  c'est une securité statless pas besoin de session coté serveur  
		 */
		http.csrf().disable();
		/** pour indiquer la page de login non celle par defaut proposé par spring security */
		//http.formLogin().loginPage("/login");
		
		/**utilisé seulement la page login par defaut de spring security 
		 * pour passer d'une authentification par reference(ref= sessionId + une instance session coté serveur) 
		 * vers une authentification par valeurs( l objet Jwt) c'est le mode stateless
		 * il faut desactiver le formulaire plus dire à spring security d'utiliser le mode
		 * sessionCreationPolicy.STATELESS
		 */		
		//http.formLogin();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/login/**","/register").permitAll(); // authorise le formulaire de login dans tous les cas le user n a pas besoin de s'authentifier sur la page login c'est logique
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/tasks/**").hasAuthority("ADMIN"); 
		http.authorizeRequests().anyRequest().authenticated();
		/** la methode authenticationManager() c'est une methode qu on herite de WebSecurityConfigurerAdapter 
		 * qui retourn l'objet principal authenticationManager
		 * pour le test il faut utiliser l url  http://localhost:8080/login  en POST
		 * et envoyer un objet Json ==> {"username" :"admin" , "password" :"1234"}
		 * en recoi le code 302 pour la redirection
		 * 
		 * -1 filter
		 */
		http.addFilter(new JWTAuthentificationFilter(authenticationManager()) );
		http.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
