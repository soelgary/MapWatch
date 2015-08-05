package com.gsoeller.personalization.maps.auth;

public class User {
	
	private String username;
	private String password;
	private Role role;
	private boolean active;

	public User() {}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public User(String username, Role role) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
