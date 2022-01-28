package br.com.contabilidadereal.deccontrol.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Service;

@Service
public class EnviarEmailService {


	    public void enviaEmail(String destinatario, String link) {
	    
	    System.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
	    final String username = "real@contabilidadereal.com.br";
	    final String password = "20crqt17";
	    
	    Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
	                new javax.mail.Authenticator() {
	                    protected PasswordAuthentication getPasswordAuthentication() {
	                        return new PasswordAuthentication(username, password);
	                    }
	                });

	        try {
	        	Multipart multipart = new MimeMultipart();
	        	MimeBodyPart messagePart = new MimeBodyPart();
	      
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress("DeControl <deccontrol@contabilidadereal.com.br>"));
	            
	            message.setRecipients(
	                    Message.RecipientType.TO,
	                    InternetAddress.parse(destinatario)
	            );
	            message.setSubject("Redefina sua senha");
	           messagePart.setContent(esqueceuSenha(link), "text/html; charset=utf-8");
	            multipart.addBodyPart(messagePart);
	            message.setContent(multipart);
	            
	          //  message.setText();

	            Transport.send(message);
	            System.out.println("Done");

	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public String esqueceuSenha(String link){
	            
	        String content = "<p>Olá,</p>"
	                    + "<p>Você solicitou uma redefinição de senha.</p>"
	                    + "<p>Clique no link abaixo para definir uma nova senha:</p>"
	                    + "<p><a href=\"" + link + "\">Altere sua senha</a></p>"
	                    + "<br>"
	                    + "<p>Ignore esse e-mail caso lembre-se da sua senha, "
	                    + "ou se você não fez a solicitação.</p>";
	             
	    return content;    
	    }


	}