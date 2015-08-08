package com.gsoeller.personalization.maps.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	
	private int id;
	private String username;
	@JsonProperty
	private String password;
	private Role role;
	private boolean active;

	public User() {}

	public User(int id, String username, String password, Role role, boolean active) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.active = active;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
	
	@JsonIgnore
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
	
	public static class UserBuilder {
		private int id;
		private String username;
		private String password;
		private Role role;
		private boolean active;
		
		public UserBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public UserBuilder setUsername(String username) {
			this.username = username;
			return this;
		}
		public UserBuilder setPassword(String password) {
			this.password = password;
			return this;
		}
		public UserBuilder setRole(Role role) {
			this.role = role;
			return this;
		}
		public UserBuilder setActive(boolean active) {
			this.active = active;
			return this;
		}
		
		public User build() {
			return new User(id, username, password, role, active);
		}
	}
}
