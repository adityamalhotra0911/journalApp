package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

@Autowired
private UserRepository userRepository;

private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

public List<User> getAll()
{
    return userRepository.findAll();
}

public boolean saveNewUser(User user)
{
    try {
        user.setPassword(passwordEncoder.encode(user.getPassword()));//isme password ko encode krke save krdiya
        user.setRoles(Arrays.asList("USER "));//roles set krdiye manually
        userRepository.save(user);
        return true;
    }
    catch(Exception e)
    {
        log.error("hahahahahaha");
        log.debug("hahahahahaha");
        return false;
    }//finally db mei save krdiya Repository ki help se
}

public void saveAdmin(User user)//Iska mtlb hai save krne se pehle User ko admin banado
{
   user.setPassword(passwordEncoder.encode(user.getPassword()));
   user.setRoles(Arrays.asList("USER","ADMIN"));
    userRepository.save(user);
}

public void saveUser(User user)
{
        // user.setDate(LocalDateTime.now());
    userRepository.save(user);//Isse db mei user save ho jaata hai
}

public Optional<User> findById(ObjectId id)
{
    return userRepository.findById(id);
}

public void deleteById(ObjectId id)
{
    userRepository.deleteById(id);
}

public User findByUsername(String username)
{
    return userRepository.findByUsername(username);
}

}
