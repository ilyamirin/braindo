package braindo.crawler;

import braindo.domain.BunchesDAO;
import braindo.domain.QuestionsDAO;
import braindo.models.Bunch;
import braindo.models.Question;
import braindo.validators.Validator;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ilyamirin on 09.02.15.
 */
@Slf4j
public class Crawler implements Runnable {

    private static final Pattern QUESTION_PATTERN = Pattern.compile("(?<=Вопрос [0-9]{1,3}:)(.+)(?>Ответ:)", Pattern.DOTALL);
    private static final Pattern ANSWER_PATTERN_SOURCE = Pattern.compile("(?<=Ответ:)(.+)(?>Источник)", Pattern.DOTALL);
    private static final Pattern ANSWER_PATTERN_AUTHOR = Pattern.compile("(?<=Ответ:)(.+)(?>Автор)", Pattern.DOTALL);
    private static final Pattern ANSWER_PATTERN_COMMENT = Pattern.compile("(?<=Ответ:)(.+)(?>Комментарий)", Pattern.DOTALL);
    private static final Pattern AUTHOR_PATTERN = Pattern.compile("(?<=Автор:)[^\\n]+");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("(?<=Комментарий:)[^\\n]+");

    private final QuestionsDAO questionsDao;

    private final BunchesDAO bunchesDAO;

    public Crawler(QuestionsDAO questionsDao, BunchesDAO bunchesDAO) {
        this.questionsDao = questionsDao;
        this.bunchesDAO = bunchesDAO;
    }

    Question parse(String rawText) {
        Question question = new Question();
        Matcher matcher = QUESTION_PATTERN.matcher(rawText);
        if (matcher.find()) {
            question.setBody(matcher.group(1).replaceAll("\\n\\.\\.\\.\\n\\n", "").trim());
        }
        matcher = ANSWER_PATTERN_AUTHOR.matcher(rawText);
        if (matcher.find()) {
            question.setAnswer(matcher.group(1).trim());
        }
        matcher = ANSWER_PATTERN_COMMENT.matcher(rawText);
        if (matcher.find()) {
            question.setAnswer(matcher.group(1).trim());
        }
        matcher = ANSWER_PATTERN_SOURCE.matcher(rawText);
        if (matcher.find()) {
            question.setAnswer(matcher.group(1).trim());
        }
        matcher = AUTHOR_PATTERN.matcher(rawText);
        if (matcher.find()) {
            question.setAuthorName(matcher.group().trim());
        }
        matcher = COMMENT_PATTERN.matcher(rawText);
        if (matcher.find()) {
            question.setComment(matcher.group().trim());
        }
        return question;
    }

    Set<String> getImages(List<WebElement> elements) {
        Set<String> result = Sets.newHashSet();
        for (WebElement element : elements) {
            result.add(element.getAttribute("src"));
        }
        return result;
    }

    private void sleep(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

    @Override
    public void run() {
        WebDriver driver = new FirefoxDriver();
        String baseUrl = "http://db.chgk.info/tour";
        driver.get(baseUrl);

        List<WebElement> groups = driver.findElements(By.cssSelector("#main ul li ul li a"));
        List<String> groupUrls = Lists.newArrayList();
        for (WebElement group : groups) {
            groupUrls.add(group.getAttribute("href"));
        }

        for (String groupUrl : groupUrls) {
            driver.get(groupUrl);

            try {
                List<WebElement> questions = driver.findElements(By.cssSelector("#main ul li a"));
                List<String> questionsUrls = Lists.newArrayList();
                for (WebElement question : questions) {
                    questionsUrls.add(question.getAttribute("href"));
                }
                for (String questionsUrl : questionsUrls) {
                    driver.get(questionsUrl);
                    sleep(2);

                    String bunchTitle = driver.findElement(By.cssSelector("#middlecontainer #main .title")).getText().trim();
                    Bunch bunch = new Bunch();
                    bunch.setUrl(driver.getCurrentUrl());
                    bunch.setName(bunchTitle);
                    ObjectId bunchId = bunchesDAO.insert(bunch);

                    driver.findElement(By.id("toggleAnswersLink")).click();
                    sleep(3);

                    for (WebElement questionElement : driver.findElements(By.cssSelector(".question"))) {
                        Question question = parse(questionElement.getText());
                        question.setImages(getImages(questionElement.findElements(By.tagName("img"))));
                        question.setBunchId(bunchId);
                        question.setValid(Validator.validate(question));
                        questionsDao.insert(question);
                    }
                    sleep(1);
                }
                sleep(1);
            } catch (Exception e) {
                log.error("Oops!", e);
            }
        }
        driver.quit();
    }
}
