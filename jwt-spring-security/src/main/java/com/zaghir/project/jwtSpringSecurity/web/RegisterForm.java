package com.zaghir.project.jwtSpringSecurity.web;

public class RegisterForm {
	
	public String username ;
	public String password ;
	public String repassword ; // c'est pour la confirmation de mots de pass
	public RegisterForm(){
		
	}
	public RegisterForm(String username, String password, String repassword) {		
		this.username = username;
		this.password = password;
		this.repassword = repassword;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRepassword() {
		return repassword;
	}
	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}
	@Override
	public String toString() {
		return "RegisterForm [username=" + username + ", password=" + password + ", repassword=" + repassword + "]";
	}
	
	

}
