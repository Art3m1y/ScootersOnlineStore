package ru.Art3m1y.shop.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.Art3m1y.shop.services.MailSenderService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailSenderServiceTest {
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    private MailSenderService mailSenderService;

    @Test
    void send_shouldSendMessageToUserEmail_whenUserRegisterOrRestorePassword() {
        mailSenderService.send("email", "subject", "text");

        verify(mailSender, atMostOnce()).send((SimpleMailMessage) any());
    }
}