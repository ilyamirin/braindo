package braindo.domain;

import braindo.models.Bunch;
import braindo.models.Game;
import braindo.models.Question;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by ilyamirin on 09.02.15.
 */
@Component
public class GamesDAO {

    private final MongoTemplate template;

    @Autowired
    @SuppressWarnings("unused")
    public GamesDAO(MongoTemplate template) {
        this.template = template;
    }

    public Game create(Bunch bunch, List<Question> questions, String client) {
        Game game = new Game();
        game.setBunchId(bunch.getId());
        game.setClient(client);
        for (Question question : questions) {
            game.getQuestions().put(question.getId().toString(), null);
        }
        template.insert(game);
        return game;
    }

    public Game findById(String gameId) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(gameId)));
        return template.findOne(query, Game.class);
    }

    public void updateGame(Game game, Boolean result, String questionId) {
        Query query = new Query(Criteria.where("_id").is(game.getId()));
        Update update = new Update();
        update.set("questions." + questionId, result);
        template.updateFirst(query, update, Game.class);
        game.getQuestions().put(questionId, result);
    }

    public void finishGame(Game game) {
        Query query = new Query(Criteria.where("_id").is(game.getId()));
        Update update = new Update();
        update.set("finishedAt", game.getFinishedAt());
        template.updateFirst(query, update, Game.class);
        game.setFinishedAt(new Date());
    }

}
