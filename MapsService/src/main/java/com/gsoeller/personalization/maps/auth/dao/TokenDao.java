package com.gsoeller.personalization.maps.auth.dao;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.gsoeller.personalization.maps.auth.Token;
import com.gsoeller.personalization.maps.auth.User;

public class TokenDao {

	private List<Token> tokens = Lists.newArrayList();
	
	public Token createToken(User user) {
		for(Iterator<Token> it = tokens.iterator(); it.hasNext();) {
			Token token = it.next();
			if(token.getUsername().equals(user.getUsername())) {
				it.remove();
			}
		}
		Token token = new Token(user.getUsername());
		tokens.add(token);
		return token;
	}
	
	public Optional<Token> getToken(User user) {
		for(Token token: tokens) {
			if(token.getUsername().equals(user.getUsername())) {
				return Optional.of(token);
			}
		}
		return Optional.absent();
	}
	
	public Optional<Token> getToken(String tokenValue) {
		for(Token token: tokens) {
			if(token.getValue().equals(tokenValue)) {
				return Optional.of(token);
			}
		}
		return Optional.absent();
	}
}
