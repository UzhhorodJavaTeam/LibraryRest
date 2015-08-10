package com.libraryrest.mail;

import com.libraryrest.models.User;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.io.StringWriter;
import java.net.InetAddress;

public class Mailer {
    private MailSender mailSender;
    private VelocityEngine velocityEngine;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void sendMail(User user, String email, String subject, String templateUrl) throws Exception{


        SimpleMailMessage message = new SimpleMailMessage();
        String hostname = InetAddress.getLocalHost().getHostAddress();

        message.setFrom(email);
        message.setTo(user.getEmail());
        message.setSubject(subject);
        Template template = null;

        template = velocityEngine.getTemplate(templateUrl);
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("user", user);
        velocityContext.put("hostname", hostname);

        StringWriter stringWriter = new StringWriter();

        template.merge(velocityContext, stringWriter);

        message.setText(stringWriter.toString());

        mailSender.send(message);
    }
}