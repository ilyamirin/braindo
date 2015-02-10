package braindo.domain;

import braindo.models.Question;
import com.google.common.base.Preconditions;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * Created by ilyamirin on 09.02.15.
 */
@Component
public class QuestionsDAO {

    private final Random r = new Random();

    private final MongoTemplate template;

    @Autowired
    @SuppressWarnings("unused")
    public QuestionsDAO(MongoTemplate template) {
        this.template = template;
    }

    public long count(boolean isValid) {
        return template.count(new Query(Criteria.where("isValid").is(isValid)), Question.class);
    }

    public void insert(Question question) {
        Preconditions.checkNotNull(question);
        template.insert(question);
    }

    public Question getRandom() {
        int skip = r.nextInt((int) count(true));
        Query query = new Query(Criteria.where("isValid").is(true)).limit(-1).skip(skip);
        return template.find(query, Question.class).get(0);
    }

    public Question findById(String id) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        return template.findOne(query, Question.class);
    }

    public List<Question> findByBunchId(ObjectId bunchId) {
        Query query = new Query(Criteria.where("bunchId").is(bunchId));
        return template.find(query, Question.class);
    }
}
