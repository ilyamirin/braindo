package braindo.models;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

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
    private String authorName;
    private String comment;
    private Long bunchId;
    private Date addedAt;

    @SuppressWarnings("unused")
    public boolean hasComment() {
        return comment != null && comment.isEmpty();
    }
}
