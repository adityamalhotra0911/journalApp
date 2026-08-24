package net.engineeringdigest.journalApp.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.enums.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@NoArgsConstructor
//@AllArgsConstructor
@Slf4j
@Document(collection="journal_entries")
public class JournalEntry {
    @Id
    private ObjectId id;//Primary Key!
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;
    private Sentiment sentiment;
}
