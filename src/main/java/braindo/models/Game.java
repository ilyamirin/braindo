package braindo.models;

import com.google.common.collect.Maps;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;


/**
 * Created by ilyamirin on 09.02.15.
 */
@Data
@Document(collection = "games")
public class Game {

    @Id
    private ObjectId id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date startedAt = new Date();
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date finishedAt;
    private ObjectId bunchId;
    private Map<String, Boolean> questions = Maps.newHashMap();
    private String client;
}
