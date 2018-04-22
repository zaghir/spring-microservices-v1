package com.zaghir.project.jwtSpringSecurity.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zaghir.project.jwtSpringSecurity.entites.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
