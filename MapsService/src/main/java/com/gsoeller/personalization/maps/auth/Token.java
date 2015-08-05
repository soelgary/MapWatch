package com.gsoeller.personalization.maps.auth;

import java.util.UUID;

import org.joda.time.DateTime;

import com.google.common.base.Objects;

public class Token {
	
	private String username;
	private DateTime created;
	private DateTime expires;
	private String value;
	
	public Token(String username) {
		this.username = username;
		created = DateTime.now();
		expires = created.plusDays(1);
		value = UUID.randomUUID().toString();
	}
	
	public String getUsername() {
		return username;
	}
	
	public DateTime getCreated() {
		return created;
	}
	
	public DateTime getExpires() {
		return expires;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object that) {
		if(that instanceof Token) {
			Token thatToken = (Token) that;
			return this.username.equals(thatToken.getUsername())
					&& this.getCreated().toString().equals(thatToken.getCreated().toString())
					&& this.getExpires().toString().equals(thatToken.getExpires().toString())
					&& this.value.equals(thatToken.getValue());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(username, created, expires, value);
	}
}
