package braindo.controllers;

import braindo.crawler.Crawler;
import braindo.domain.BunchesDAO;
import braindo.domain.QuestionsDAO;
import braindo.models.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ilyamirin on 09.02.15.
 */

@Slf4j
@Controller
@SuppressWarnings("unused")
public class MainController {

    @Autowired
    private BunchesDAO bunchesDAO;

    @Autowired
    private QuestionsDAO questionsDAO;

    @RequestMapping("/")
    String home(Model model) {
        Question question = questionsDAO.getRandom();
        model.addAttribute("question", question);
        return "main";
    }

    @RequestMapping("/parse")
    @ResponseBody
    String parse() {
        new Thread(new Crawler(questionsDAO, bunchesDAO)).start();
        return "Started!";
    }
}
