package braindo.controllers;

import braindo.domain.QuestionsDAO;
import braindo.domain.QuestionsRepository;
import braindo.models.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ilyamirin on 09.02.15.
 */

@Slf4j
@Controller
public class MainController {

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private QuestionsDAO questionsDAO;

    @RequestMapping("/")
    String home(Model model) {
        Question questionToSave = new Question();
        questionToSave.setBody("Вопрос");
        questionToSave.setAnswer("Ответ");
        questionToSave.setAuthorName("Неизвестноктожка");
        questionToSave.setBunchId(1l);
        questionsDAO.insert(questionToSave);

        Question question = questionsDAO.getRandom();
        model.addAttribute("question", question);
        return "main";
    }
}
