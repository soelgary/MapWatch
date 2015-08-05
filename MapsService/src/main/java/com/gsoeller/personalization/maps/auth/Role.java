package com.gsoeller.personalization.maps.auth;

public enum Role {
	ADMIN(Role.ADMIN_ROLE), // read and write permissions
	RESEARCHER(Role.RESEARCHER_ROLE); // read permissions only
	
	public static final String ADMIN_ROLE = "Admin";
	public static final String RESEARCHER_ROLE = "Researcher";
	
	private String role;
	
	private Role(String role) {
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}
}
