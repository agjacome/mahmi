package es.uvigo.ei.sing.mahmi.common.utils.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * {@linkplain MailSender} is a class that sends email's
 * 
 * @author Aitor Blanco-Míguez
 *
 */
@Slf4j
public class MailSender {
	private final Properties properties = new Properties();
	private final String username = "noreplyp4p@gmail.com";
	private final String password = "p4psing33$";

	/**
	 * Initializes the {@linkplain MailSender}
	 * 
	 * @return true if all is OK
	 */
	public boolean init() {
		this.properties.put("mail.smtp.auth", "true");
		this.properties.put("mail.smtp.starttls.enable", "true");
		this.properties.put("mail.smtp.host", "smtp.gmail.com");
		this.properties.put("mail.smtp.port", "587");
		return true;
	}

	/**
	 * Sends an email
	 * 
	 * @param destination
	 *            The destination of the email
	 * @param subject
	 *            The subject of the email
	 * @param text
	 *            The main text of the email
	 */
	public void send(final String destination, final String subject, final String text) {

		final Session session = Session.getInstance(this.properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			final Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destination));
			message.setSubject(subject);
			message.setText(text);

			Transport.send(message);

			log.info("Message sended successfully:\nTo: " + destination + "\nSubject: " + subject);

		} catch (MessagingException e) {
			log.error(e.getMessage());
		}
	}
}
