package braindo.crawler;

import braindo.models.Question;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by ilyamirin on 10.02.15.
 */
@Slf4j
public class CrawlerTest {

    @Test
    public void questionParseTest() {
        String seq = "Вопрос 15: Закончите стихотворение Эли Бар-Яалома:\n" +
                "    Свежим родителям некогда жить:\n" +
                "    Надо все время стирать и сушить.\n" +
                "    Чтоб им полегче немножечко было,\n" +
                "    Мы им подарим...\n" +
                "\n" +
                "Ответ: Веревку и мыло.\n" +
                "\n" +
                "Комментарий: На этой оптимистической ноте мы заканчиваем второй тур.\n" +
                "\n" +
                "Источник(и): http://imenno.livejournal.com/70859.html\n" +
                "\n" +
                "Автор: Михаил Иванов (Саратов)";

        Crawler crawler = new Crawler(null, null);

        Question question = crawler.parse(seq);
        log.error(question.toString());
        assert question.getBody() != null;
        assert question.getBody().contains("Надо все время стирать и сушить");
        assert !question.getBody().contains("Веревку и мыло");
    }
}
