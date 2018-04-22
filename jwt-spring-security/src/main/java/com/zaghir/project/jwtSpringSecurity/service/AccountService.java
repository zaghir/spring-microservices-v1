package com.zaghir.project.jwtSpringSecurity.service;

import com.zaghir.project.jwtSpringSecurity.entites.AppRole;
import com.zaghir.project.jwtSpringSecurity.entites.AppUser;

public interface AccountService {
	public AppUser saveUser(AppUser user );
	public AppRole saveRole(AppRole role);
	public void addRoleToUser(String username , String roleName);
	
	public AppUser findUserByUsername(String username);

}
