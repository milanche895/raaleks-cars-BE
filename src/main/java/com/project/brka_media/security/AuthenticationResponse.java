package com.project.brka_media.security;

import java.util.Collection;

public class AuthenticationResponse {

	private String token;

	private Collection role;
	
	public AuthenticationResponse()
	{
		
	}

	public AuthenticationResponse(String token, Collection role) {
		super();
		this.token = token;
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Collection getRole() {
		return role;
	}

	public void setRole(Collection role) {
		this.role = role;
	}
}
