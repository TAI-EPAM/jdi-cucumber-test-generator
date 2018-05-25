package com.epam.test_generator.services;

import com.epam.test_generator.controllers.user.UserDTOsTransformer;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
@Service
@Transactional
@PropertySource("classpath:email.messages.properties")
@Profile("!integration-tests")
public class EmailServiceImpl implements EmailService {


    @Autowired
    private PasswordService passwordService;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDTOsTransformer userDTOsTransformer;

    @Resource
    private Environment environment;

    /**
     * Sends message to register user
     * @param user entity of user who will be registered
     * @param request
     */
    @Override
    public UserDTO sendRegistrationMessage(User user, HttpServletRequest request) {
        Token userConformationToken = tokenService.createToken(user, CONFIRMATION_TIME);
        String confirmUrl = passwordService.createConfirmUrl(request, userConformationToken);
        String subject = environment.getProperty("subject.registration.message");
        String text = environment.getProperty("registration.message");
        String site = environment.getProperty("site.name");
        text = String.format(text, user.getName(), user.getSurname(), site,
            confirmUrl);
        sendSimpleMessage(user.getEmail(), subject, text);
        return userDTOsTransformer.toUserDTO(user);
    }

    /**
     * Sends message to reset password for user
     * @param user entity of user for who password will be reset
     * @param request
     */
    @Override
    public void sendResetPasswordMessage(User user, HttpServletRequest request) {
        Token token = tokenService.createToken(user, PASSWORD_RESET_TIME);
        String resetUrl = passwordService.createResetUrl(request, token);
        String subject = environment.getProperty("subject.password.message");
        String text = environment.getProperty("reset.password.message");
        text = String.format(text, user.getName(), user.getSurname(), resetUrl,
            javaMailSender.getUsername());
        sendSimpleMessage(user.getEmail(), subject, text);
    }

    /**
     * Creates SimpleMailMessage from params and sends it via emailSender.send() method
     * @param to addressee
     * @param subject message subject
     * @param text message text
     */
    private void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}