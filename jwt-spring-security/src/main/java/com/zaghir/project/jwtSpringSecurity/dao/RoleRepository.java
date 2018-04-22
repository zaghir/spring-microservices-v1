package com.zaghir.project.jwtSpringSecurity.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zaghir.project.jwtSpringSecurity.entites.AppRole;

public interface RoleRepository extends JpaRepository<AppRole, Long>{
	public AppRole findByRoleName(String roleName);
}
