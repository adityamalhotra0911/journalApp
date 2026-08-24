package net.engineeringdigest.journalApp.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Slf4j
@Builder
@Document(collection="users")
public class User {
    @Id
    private ObjectId id;
    @Indexed(unique=true)
    @NonNull
    private String username;
    private String email;
    private boolean sentimentAnalysis;
    @NonNull
    private String password;
    private LocalDateTime date;

    @DBRef//creating reference in user collection of journalEntries!!! Reference rakhegi
    private List<JournalEntry> journalEntries=new ArrayList<>();
    private List<String> roles;
}
