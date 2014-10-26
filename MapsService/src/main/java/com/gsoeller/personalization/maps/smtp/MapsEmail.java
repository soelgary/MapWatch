package com.gsoeller.personalization.maps.smtp;

import java.io.IOException;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.google.common.collect.Lists;
import com.gsoeller.personalization.maps.PropertiesLoader;

public class MapsEmail {
	private final String hostname;
	private final int port;
	private final boolean useSSL;
	private final String username;
	private final String password;
	private final String from;
	private final List<String> to;
	private final String subject;
	private final String message;

	private MapsEmail(String hostname, int port, boolean useSSL,
			String username, String password, String from, List<String> to,
			String subject, String message) {
		this.hostname = hostname;
		this.port = port;
		this.useSSL = useSSL;
		this.password = password;
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.message = message;
		this.username = username;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFrom() {
		return from;
	}

	public List<String> getTo() {
		return to;
	}

	public String getSubject() {
		return subject;
	}

	public String getMessage() {
		return message;
	}
	
	public Email createEmail() throws EmailException {
		Email email = new SimpleEmail();
		email.setHostName(hostname);
		email.setSmtpPort(port);
		email.setAuthenticator(new DefaultAuthenticator(username, password));
		email.setSSL(useSSL);
		email.setFrom(from);
		email.setSubject(subject);
		email.setMsg(message);
		for(String sendTo: to) {
			email.addTo(sendTo);
		}
		return email;
	}

	public class MapsEmailBuilder {
		private String hostname;
		private int port = 465;
		private boolean useSSL = true;
		private String username;
		private String password;
		private String from;
		private List<String> to = Lists.newArrayList();
		private String subject;
		private String message;

		public MapsEmailBuilder() throws IOException {
			PropertiesLoader loader = new PropertiesLoader();
			username = loader.getProperty("username");
			password = loader.getProperty("password");
			hostname = loader.getProperty("server");
			from = loader.getProperty("from");
		}

		public MapsEmailBuilder setFrom(String from) {
			this.from = from;
			return this;
		}

		public MapsEmailBuilder addTo(String to) {
			this.to.add(to);
			return this;
		}

		public MapsEmailBuilder setSubject(String subject) {
			this.subject = subject;
			return this;
		}

		public MapsEmailBuilder setmMessage(String message) {
			this.message = message;
			return this;
		}

		public MapsEmailBuilder setHostName(String hostname) {
			this.hostname = hostname;
			return this;
		}

		public MapsEmail build() {
			return new MapsEmail(hostname, port, useSSL, username, password,
					from, to, subject, message);
		}
	}
}
