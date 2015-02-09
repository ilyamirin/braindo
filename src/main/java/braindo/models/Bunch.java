package braindo.models;

import com.google.common.collect.Lists;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by ilyamirin on 09.02.15.
 */
@Data
public class Bunch {

    private ObjectId id;
    private String name;
    private List<Long> questionsIds = Lists.newArrayList();
}
