package io.anonsousa.email_ms.services;

import io.anonsousa.email_ms.enums.StatusEmail;
import io.anonsousa.email_ms.models.EmailModel;
import io.anonsousa.email_ms.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    JavaMailSender mailSender;

    @Value(value = "${spring.mail.username}" )
    private String emailFrom;

    @Transactional
    public EmailModel sendEmail(EmailModel emailModel){
        try{
            emailModel.setSendDateEmail(LocalDateTime.now());
            emailModel.setEmailFrom(emailFrom);

            var mailMessage = new SimpleMailMessage();
            mailMessage.setTo(emailModel.getEmailTo());
            mailMessage.setSubject(emailModel.getSubject());
            mailMessage.setText(emailModel.getText());
            mailSender.send(mailMessage);


            emailModel.setStatusEmail(StatusEmail.SENT);
        } catch (MailException me){
            emailModel.setStatusEmail(StatusEmail.ERROR);
        } finally {
            return emailRepository.save(emailModel);
        }
    }

















}
