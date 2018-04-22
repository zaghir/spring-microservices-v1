package com.zaghir.project.jwtSpringSecurity.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zaghir.project.jwtSpringSecurity.entites.AppRole;
import com.zaghir.project.jwtSpringSecurity.entites.AppUser;
import com.zaghir.project.jwtSpringSecurity.service.AccountService;

@RestController
public class AccountRestController {
	
	@Autowired
	private AccountService accountService ;
	
	@PostMapping("/register")
	public AppUser register(@RequestBody RegisterForm userForm){
		/**
		 * pour resoudre le probleme de password envoyé dans l'objet Json 
		 * on Ajoute JsonIgnore sur la methode get , et JsonSetter sur la methode set
		 * il faut savoir que la methode JsonIgnore cree un probleme si on veut confirmer le pass dans le front car il n'est plus serialisé
		 * pour ca en passe par java on creant une nouvelle classe RegisterForm et charger les valeurs depuis java et non de puis Json  pas mal !
		 * on va faire des verifications avant d'enregister le user
		 */
		/** on verifie si le password est bien confirmé*/
		if(!userForm.getPassword().equals(userForm.getRepassword())){
			throw new RuntimeException("You must confirm your password");
		}
		/** on verifie si le user existe deja dans la base*/
		AppUser user = accountService.findUserByUsername(userForm.getUsername());
		if(user !=null){
			throw new RuntimeException("this user already existes");
		}
		AppUser appUser = new AppUser();
		appUser.setUsername(userForm.getUsername());
		appUser.setPassword(userForm.getPassword());
		accountService.saveUser(appUser);
		/** on ajoute un role par defaut
		 * apres on peut modifier le role vers ADMIN ou .... 
		 *  */
		accountService.addRoleToUser(userForm.getUsername(), "USER");
		
		return appUser;
	}

}
