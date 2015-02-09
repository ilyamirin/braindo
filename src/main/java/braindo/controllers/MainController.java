package braindo.controllers;

import braindo.crawler.Crawler;
import braindo.domain.BunchesDAO;
import braindo.domain.GamesDAO;
import braindo.domain.QuestionsDAO;
import braindo.models.Bunch;
import braindo.models.Game;
import braindo.models.Question;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by ilyamirin on 09.02.15.
 */

@Slf4j
@Controller
@SuppressWarnings("unused")
public class MainController {

    private static final String GAME_COOKIE_NAME = "BRAINDO_GAME_ID";

    @Autowired
    private BunchesDAO bunchesDAO;

    @Autowired
    private QuestionsDAO questionsDAO;

    @Autowired
    private GamesDAO gamesDAO;

    @RequestMapping("/")
    String main(Model model) {
        Question question = questionsDAO.getRandom();
        model.addAttribute("question", question);
        return "main";
    }

    @RequestMapping("/bunches")
    String bunches(Model model) {
        List<Bunch> bunches = bunchesDAO.findAll();
        model.addAttribute("bunches", bunches);
        return "bunches";
    }

    @RequestMapping("/play")
    String play(@CookieValue(value = GAME_COOKIE_NAME, required = false) String gameId,
                @RequestHeader(value="User-Agent") String userAgent,
                @RequestParam("bunchId") String bunchId,
                @RequestParam(value = "result", required = false) Boolean result,
                @RequestParam(value = "questionId", required = false) String questionId,
                HttpServletResponse response,
                Model model) {
        Game game;
        if (gameId == null) {
            Bunch bunch = bunchesDAO.findById(bunchId);
            List<Question> questions = questionsDAO.findByBunchId(bunch.getId());
            game = gamesDAO.create(bunch, questions, userAgent);
            response.addCookie(new Cookie(GAME_COOKIE_NAME, game.getId().toString()));
        } else {
            game = gamesDAO.findById(gameId);
        }

        Preconditions.checkNotNull(game);

        if (result != null && questionId != null) {
            gamesDAO.updateGame(game, result, questionId);
        }

        Question question = null;
        for (Map.Entry<String, Boolean> entry : game.getQuestions().entrySet()) {
            if (entry.getValue() == null) {
                question = questionsDAO.findById(entry.getKey());
                break;
            }
        }

        if (question != null) {
            model.addAttribute("question", question);
            return "main";
        } else {
            gamesDAO.finishGame(game);
            response.addCookie(new Cookie(GAME_COOKIE_NAME, null));
            return "results";
        }
    }

    @RequestMapping("/parse")
    @ResponseBody
    String parse() {
        new Thread(new Crawler(questionsDAO, bunchesDAO)).start();
        return "Started!";
    }
}
