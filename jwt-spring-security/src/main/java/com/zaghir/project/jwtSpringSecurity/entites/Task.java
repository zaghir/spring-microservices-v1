package com.zaghir.project.jwtSpringSecurity.entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Task {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
	private String taskName;
	
	public Task() {
		
	}
	public Task(Long id, String taskName) {		
		this.id = id;
		this.taskName = taskName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	@Override
	public String toString() {
		return "Task [id=" + id + ", taskName=" + taskName + "]";
	}
	

}
