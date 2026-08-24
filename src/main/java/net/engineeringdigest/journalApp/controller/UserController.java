package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.EmailService;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private EmailService emailService;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();//Isse sirf name mila
        User userInDb = userService.findByUsername(username);//Isse poora object mil gaya jisme sab kuch hai
        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);//save krne se pehle password ko encode krdiya and then save kiya
        return new ResponseEntity<>(HttpStatus.OK);//http status return krdiya
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById(@RequestBody User user)//JSON bhejna hoga
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName());//deleteByUsername humne khud se banani hai repo mei
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> greeting()//Wild card !!
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");//Default
        String greeting = "";//Add krne ke liye

        if (weatherResponse != null) {
            greeting = ", Weather feels like " + weatherResponse.getCurrent().getFeelslike();
        }

        return new ResponseEntity<>("Hi " + authentication.getName() + greeting, HttpStatus.OK);
    }

    @PostMapping("/test-mail")
    public void testMail() {
        // Apni dusri email id yahan daal kar check kar
        emailService.sendEmail("adityamalhotra.911@gmail.com", "Test Mail", "Bhai, Spring Boot se mail aa gaya!");
    }
}
