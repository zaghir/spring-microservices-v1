package com.zaghir.project.jwtSpringSecurity.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zaghir.project.jwtSpringSecurity.entites.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long>{
	public AppUser findByUsername(String username);
}
