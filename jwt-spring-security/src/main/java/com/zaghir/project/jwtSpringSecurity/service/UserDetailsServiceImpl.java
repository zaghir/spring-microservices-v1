package com.zaghir.project.jwtSpringSecurity.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zaghir.project.jwtSpringSecurity.entites.AppUser;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AccountService accountService ;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user= accountService.findUserByUsername(username);
		/** si on ne trouve pas le user e=on envoie une exception de type UsernameNotFoundException */
		if(user== null){  
			throw new UsernameNotFoundException(username);
		}
		
		/** si non il faut recupere les role de le user 
		 * creer un objet technique User de type spring  
		 * et ajouter les authorité
		 */
		Collection<GrantedAuthority> authorites = new ArrayList<>();
		user.getRoles().forEach(r->{  // c'est par egger Jpa qu on recupere les roles super!
			authorites.add(new SimpleGrantedAuthority(r.getRoleName()));
		});
			
		return new User(user.getUsername(), user.getPassword() ,authorites);
	} 

}
