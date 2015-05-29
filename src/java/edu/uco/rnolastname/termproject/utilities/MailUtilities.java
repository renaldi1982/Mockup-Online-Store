package edu.uco.rnolastname.termproject.utilities;

import java.util.UUID;
import javax.annotation.Resource;
import javax.mail.BodyPart;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage;

public class MailUtilities {        
    
    private static InternetAddress fromEmail;
             
        
	public static void sendEmail(Session mailSession,String to,
            String subject,String firstName,String text) throws AddressException,MessagingException{		

            fromEmail = new InternetAddress("barnesandtoddler@gmail.com");
            InternetAddress toEmail = new InternetAddress(to);
            
            Message msg = new MimeMessage(mailSession);
            msg.setSubject(subject);
            msg.setRecipient(RecipientType.TO,toEmail);
            msg.setFrom(fromEmail);

            BodyPart msgBody = new MimeBodyPart();
            msgBody.setText(text);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(msgBody);

            msg.setContent(multipart);

            Transport.send(msg);                	                        
	}	
        
        public static String generateUUID(){
            String uuid = UUID.randomUUID().toString();            
            return uuid;
        }
}
