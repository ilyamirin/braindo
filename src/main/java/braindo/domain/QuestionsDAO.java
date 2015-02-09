package braindo.domain;

import braindo.models.Question;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

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

    public long count() {
        return template.count(new Query(), Question.class);
    }

    public void insert(Question question) {
        Preconditions.checkNotNull(question);
        template.insert(question);
    }

    public Question getRandom() {
        int skip = r.nextInt((int) count());
        Query query = new Query().limit(-1).skip(skip);
        return template.find(query, Question.class).get(0);
    }
}
