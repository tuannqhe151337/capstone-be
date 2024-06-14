package com.example.capstone_project.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class EmailService {
    private final JavaMailSender mailSender;


    @Async
    public void sendEmail(String to, String subject, String username, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("Account information for " + subject );

        String htmlContent = "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        /* Định dạng cho phần nội dung chính */\n" +
                "        .highlight {\n" +
                "            font-weight: bold;\n" +
                "            color: #007bff;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h3>This is a message just for you, <i>" + subject +"</i>. </h3>\n" +
                "    <p>Here are your login details:</p>\n" +
                "    <p>Your account username is: <span class=\"highlight\"> " +username+ "</span></p>\n" +
                "    <p>Your account password is: <span class=\"highlight\">" + password+"</span></p>\n" +

                "    <p>Thanks a lot, and I hope you have a fantastic experience with Financial Planner Website</p>\n\n" +
                "    <p>Best, </p>\n" +
                "    <p><i>Admin</i></p>\n" +
                "</body>\n" +
                "</html>";

        message.setContent(htmlContent, "text/html; charset=utf-8");


        mailSender.send(message);

    }
}
