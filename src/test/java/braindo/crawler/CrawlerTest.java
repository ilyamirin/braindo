package braindo.crawler;

import braindo.models.Question;
import braindo.validators.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by ilyamirin on 10.02.15.
 */
@Slf4j
public class CrawlerTest {

    @Test
    public void questionParseTest() {
        Crawler crawler = new Crawler(null, null);

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
        Question question = crawler.parse(seq);
        assert question.getBody() != null;
        assert question.getBody().contains("Надо все время стирать и сушить");
        assert !question.getBody().contains("Веревку и мыло");
        assert question.getAnswer().contains("Веревку и мыло");

        seq = " Вопрос 3: В последней сцене постмодернистской пьесы Брайана Олдиса русский поэт Фирковский что-то бормочет под звуки уничтожения ЕГО. Назовите ЕГО двумя словами.\n" +
                "...\n" +
                "\n" +
                "Ответ: Вишневый сад.\n" +
                "\n" +
                "Комментарий: Вся сцена — пародия на финальную сцену пьесы. Фирковский — Фирс.\n" +
                "\n" +
                "Источник(и): Aldiss B. The Impossible Puppet Show, (ss) Factions, ed. Giles Gordon & Alex Hamilton, 1974.\n" +
                "\n" +
                "Автор: Иван Белецкий (Краснодар) ";
        question = crawler.parse(seq);
        assert question.getBody() != null;
        assert question.getBody().contains("поэт Фирковский что-то бормочет");
        assert !question.getBody().contains("Aldiss B.");
        assert question.getAuthorName().contains("Иван Белецкий");
        assert question.getAnswer().contains("Вишневый сад.");
        assert !question.getAnswer().contains("Aldiss");
        assert !question.getAnswer().contains("Белецкий");

        seq = " Вопрос 123:\n" +
                "    \"Вошла, как гостья, и осталась\n" +
                "    И, как служанка, нам верна,\n" +
                "    С ней жить удобно оказалось,\n" +
                "    Замена разуму она.\"\n" +
                "    Рене-Франсуа-Арман Сюлли-Прюдом сказал это о привычке. В 1901 году автор этих строк сумел превзойти таких корифеев слова, как Джеймс Джойс, Лев Толстой, Эмиль Золя, Марсель Пруст, а Академия Наук получила гневное письмо-протест за подписью Сельмы Лагерлеф, Юханна Стриндберга, Карла Хейденстамма. Впрочем, академики, возможно, и не были столь неправы — ведь эти стихи любил один человек. Назовите его.\n" +
                "...\n" +
                "\n" +
                "Ответ: Альфред Нобель.\n" +
                "\n" +
                "Автор: Ровшан Аскеров ";
        question = crawler.parse(seq);
        assert question.getBody() != null;
        assert question.getBody().contains("С ней жить удобно оказалось,");
        assert !question.getBody().endsWith(" ...");
        assert !question.getBody().contains("Ответ: Альф");
        assert question.getAnswer().contains("Альфред Нобель.");

        seq = " Вопрос 3: Рафаэль, Микеланджело, Леонардо... А кто же был в их компании четвёртым?\n" +
                "...\n" +
                "\n" +
                "Ответ: Донателло. Это имена ниндзя-черепашек.\n" +
                "\n" +
                "Автор: Борис Бурда ";
        question = crawler.parse(seq);
        assert question.getBody() != null;
        assert question.getBody().contains(" Леонардо... А кто же бы");
        assert !question.getBody().matches(".+\\n\\.\\.\\.");
        assert !question.getBody().contains("Донателло. ");
        assert question.getAnswer().contains("Донателло. Это имена ниндзя");

        seq = " Вопрос 50:\n" +
                "    \"Чтоб свергнуть гнет рукой умелой,\n" +
                "    Отвоевать свое добро,\n" +
                "    Вздувайте горн и куйте смело,\n" +
                "    Пока железо горячо!\"\n" +
                "    Процитируйте первые две строчки этого произведения.\n" +
                "...\n" +
                "\n" +
                "Ответ:\n" +
                "    \"Вставай, проклятьем заклейменный,\n" +
                "    Весь мир голодных и рабов!\"\n" +
                "\n" +
                "Источник(и): \"Интернационал\".\n" +
                "\n" +
                "Автор: Наталья Манусаджян ";
        question = crawler.parse(seq);
        assert question.getBody() != null;
        assert question.getBody().contains("Чтоб свергнуть гнет рукой умелой,");
        assert !question.getBody().matches(".+\\n\\.\\.\\.");
        assert !question.getBody().contains("Вставай, проклятьем заклейменный,");
        assert question.getAnswer().contains("Вставай, проклятьем заклейменный,");
        assert !question.getAnswer().contains("Интернационал");
        assert question.getAnswer().contains("Весь мир голодных и рабов!");

        seq = " Вопрос 19: Однажды поэт Потёмкин, войдя в ресторан \"Вена\", увидел за столиком двух девиц, о чём и сказал в первой строке своего стихотворного экспромта. Потом добавил всем известное латинское выражение, и получился панторим. Назовите хотя бы одну из двух его строк.\n" +
                "...\n" +
                "\n" +
                "Ответ:\n" +
                "    В \"Вене\" две девицы.\n" +
                "    Veni, vidi, vici.\n" +
                "\n" +
                "Комментарий: Панторим — стихотворная форма, в которой рифмуются все слова, входящие в строку.\n" +
                "\n" +
                "Источник(и): Мысль, вооружённая рифмами. — Л., Изд-во Ленинградского университета, 1983, с. 24\n" +
                "\n" +
                "Автор: Григорий Остров ";
        question = crawler.parse(seq);
        assert question.getBody() != null;
        assert question.getBody().contains("войдя в ресторан \"Вена\"");
        assert !question.getBody().matches(".+\\n\\.\\.\\.");
        assert !question.getBody().contains("В \"Вене\" две девицы.");
        assert question.getAnswer().contains("В \"Вене\" две девицы.");
        assert question.getAnswer().contains("Veni, vidi, vici.");
        assert question.getAuthorName().equals("Григорий Остров");

        seq = " Вопрос 28: Какая флорентийская вещь стала в наше время голландской?\n" +
                "\n" +
                "Ответ: Монета — флорин.\n" +
                "\n" +
                "Источник(и): СЭС, М., \"Советская энциклопедия\", 1989, с.1435.\n" +
                "\n" +
                "Автор: Борис Бурда ";
        question = crawler.parse(seq);
        log.error(question.toString());
        assert question.getBody() != null;
        assert question.getBody().equals("Какая флорентийская вещь стала в наше время голландской?");
        assert question.getAnswer().equals("Монета — флорин.");
        assert question.getAuthorName().equals("Борис Бурда");
    }
}
