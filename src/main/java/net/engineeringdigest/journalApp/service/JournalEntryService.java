package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
@Autowired
private JournalEntryRepository journalEntryRepository;

@Autowired
private UserService userService;

@Transactional
public void saveEntry(JournalEntry journalEntry, String username)//save modificatons!
{
    try
    {
        User user = userService.findByUsername(username);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(journalEntry);//JournalEntry wale collection mei save hui
        user.getJournalEntries().add(saved);//yahan user mei save hui hai
        userService.saveUser(user);
    }
    catch (Exception e)
    {
        throw new RuntimeException(e);
    }
}

    public void saveEntry(JournalEntry journalEntry)//save modificatons!
    {
        journalEntryRepository.save(journalEntry);
    }

public List<JournalEntry> getAll()
{
    return journalEntryRepository.findAll();
}

public Optional<JournalEntry> findById(ObjectId id)
{
    return journalEntryRepository.findById(id);
}

@Transactional
public boolean deleteById(ObjectId id, String username)// Important!!!
{
    boolean removed=false;
    try {
        User user = userService.findByUsername(username);
        removed=user.getJournalEntries()
                .removeIf(x -> x.getId()
                        .equals(id));//Turant db se remove krne ke liye!
        if(removed)
        {
            userService.saveUser(user);//Isse user mei entry delete hone ke baad save ho jayega
            journalEntryRepository.deleteById(id);//isse journal entry delete ho jayegi!!
        }
    } catch (Exception e) {
        System.out.println(e);
        throw new RuntimeException("An error occurred by deleting the entry",e);
    }
    return removed;
}
}
