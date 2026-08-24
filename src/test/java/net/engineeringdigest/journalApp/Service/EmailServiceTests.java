package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests
{
    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail()
    {
        emailService.sendEmail("adityamalhotra.911@gmail.com","Testing Java mail sender","Hello Aditya");
    }
}
