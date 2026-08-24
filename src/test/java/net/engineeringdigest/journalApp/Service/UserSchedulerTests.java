package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.scheduler.UserScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserSchedulerTests
{

    @Autowired
    private UserScheduler userScheduler;

    @Test
    public void testFetchUsersAndSendMail()
    {
        userScheduler.fetchUsersAndSendsSaMail();
    }
}
