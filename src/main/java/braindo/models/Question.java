package braindo.models;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by ilyamirin on 09.02.15.
 */
@Data
@Document(collection = "questions")
public class Question {

    @Id
    private ObjectId id;
    private String body;
    private String answer;
    @Indexed
    private String authorName;
    private String comment;
    @Indexed
    private ObjectId bunchId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date addedAt = new Date();
    @Indexed
    private boolean isValid;

    @SuppressWarnings("unused")
    public boolean hasComment() {
        return comment != null && comment.isEmpty();
    }
}
