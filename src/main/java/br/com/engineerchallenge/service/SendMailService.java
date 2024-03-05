package br.com.engineerchallenge.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class SendMailService {
	
		private final JavaMailSender javamailsender;
		
		public void senMail(String stTo, String subject, String content) {
			
			log.info("Sending Mail");
			
			SimpleMailMessage mensages = new SimpleMailMessage();
			mensages.setTo(stTo);
			mensages.setSubject(subject);
			mensages.setText(content);
			
			javamailsender.send(mensages);
		}
}