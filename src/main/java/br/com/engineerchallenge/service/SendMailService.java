package br.com.engineerchallenge.service;

import java.io.IOException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public abstract class SendMailService{
	
		private final JavaMailSender javamailsender;
		
		
		  public SendMailService(final JavaMailSender javamailsender) {
		  this.javamailsender = javamailsender;
		  
		  }
		 
		public void senMail(String stTo, String subject, String content) throws IOException {
			
			log.info("Sending Mail");
			
			SimpleMailMessage mensages = new SimpleMailMessage();
			mensages.setTo(stTo);
			mensages.setSubject(subject);
			mensages.setText(content);
			
			javamailsender.send(mensages);
		}
}