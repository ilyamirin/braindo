package braindo.domain;

import braindo.models.Question;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

/**
 * Created by ilyamirin on 09.02.15.
 */
@Component
public interface QuestionsRepository extends Repository<Question, Long> {

    Long count();
}
