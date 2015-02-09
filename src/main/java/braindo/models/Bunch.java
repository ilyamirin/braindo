package braindo.models;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

/**
 * Created by ilyamirin on 09.02.15.
 */
@Data
@Document(collection = "bunches")
public class Bunch {

    @Id
    private ObjectId id;
    private String name;
    private String url;
}
