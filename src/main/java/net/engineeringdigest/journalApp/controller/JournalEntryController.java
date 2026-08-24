package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController
{
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping// Get method is for data read/dekhne ke liye
    public ResponseEntity<?> getAllJournalEntriesOfUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);//User Object milgaya
        List<JournalEntry> all = user.getJournalEntries();//Isse us user ki journalEntries milgyi
        if(all!=null && !all.isEmpty())
        {
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry newEntry)
    {
        try
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();//yahan se username milgya
            newEntry.setDate(LocalDateTime.now());//for Updation
            journalEntryService.saveEntry(newEntry,username);//us user ki journalEntry mei add hogya
            return new ResponseEntity<>(newEntry,HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    // URL ke andar se value uthana!!!
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId)//IMPORTANT
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();//Username milgya
        User user = userService.findByUsername(username);//User ki POJO mil gyi
        List<JournalEntry> collect=user.getJournalEntries()
                .stream()//Stream mei break ho gaye saare!!
                .filter(x->x.getId()
                        .equals(myId))//Filter karliya ki kaunsi match ho rahi hai JournalEntry
                .collect(Collectors.toList());//jo match ho gayi usko collect karliya Collectors.toList() ki help se
        if(!collect.isEmpty())//agar nahi mili toh yeh uske liye?!?!?!
        {
            Optional<JournalEntry> journalEntry=journalEntryService.findById(myId);//Optional returns Boolean
            if(journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{username}/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId,@PathVariable String username) {//Method ka naam kuch bhi rakhdo
        boolean removed=journalEntryService.deleteById(myId,username);
        if(removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId myId, @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> collect = user.getJournalEntries()
                .stream()//Stream mei convert krdiya
                .filter(x -> x.getId()
                        .equals(myId))//Filter krdiya ki konsa equal hai
                .collect(Collectors.toList());//Then collect kar diya

        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            if (journalEntry.isPresent()) {
                JournalEntry old = journalEntry.get();//Isse current JournalEntry poori aa gayi
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
