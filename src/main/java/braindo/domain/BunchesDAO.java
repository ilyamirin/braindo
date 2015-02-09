package braindo.domain;

import braindo.models.Bunch;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ilyamirin on 09.02.15.
 */
@Component
public class BunchesDAO {

    private final MongoTemplate template;

    @Autowired
    @SuppressWarnings("unused")
    public BunchesDAO(MongoTemplate template) {
        this.template = template;
    }

    public ObjectId insert(Bunch bunch) {
        template.insert(bunch);
        return bunch.getId();
    }

    public List<Bunch> findAll() {
        return template.findAll(Bunch.class);
    }

    public Bunch findById(String id) {
        return template.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))), Bunch.class);
    }
}
