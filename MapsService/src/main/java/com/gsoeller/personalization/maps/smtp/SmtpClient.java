package com.gsoeller.personalization.maps.smtp;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmtpClient {
	
	private static final Logger LOG = LoggerFactory.getLogger(SmtpClient.class);

	public void sendEmail(MapsEmail mapsEmail) {
		try {
			mapsEmail.createEmail().send();
		} catch (EmailException e) {
			e.printStackTrace();
			LOG.error("Failed to send email with message, %s, and subject, %s.", mapsEmail.getMessage(), mapsEmail.getSubject());
		}
	}
}
