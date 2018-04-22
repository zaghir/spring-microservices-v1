package com.zaghir.project.jwtSpringSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zaghir.project.jwtSpringSecurity.dao.RoleRepository;
import com.zaghir.project.jwtSpringSecurity.dao.UserRepository;
import com.zaghir.project.jwtSpringSecurity.entites.AppRole;
import com.zaghir.project.jwtSpringSecurity.entites.AppUser;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	private BCryptPasswordEncoder bCryptePasswordEncoder ;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository ;
	
	@Override
	public AppUser saveUser(AppUser user) {
		String hashPws = bCryptePasswordEncoder.encode(user.getPassword());
		user.setPassword(hashPws);
		
		return userRepository.save(user);
	}

	@Override
	public AppRole saveRole(AppRole role) {
		// TODO Auto-generated method stub
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		AppRole role = roleRepository.findByRoleName(roleName);
		AppUser user = userRepository.findByUsername(username);
		user.getRoles().add(role); // la methode est transactionnel du coup des qu elle fait un commite elle ajoute le role à user dans la base 
	}

	@Override
	public AppUser findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
