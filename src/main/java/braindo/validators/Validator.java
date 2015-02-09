package braindo.validators;

import braindo.models.Question;

/**
 * Created by ilyamirin on 09.02.15.
 */
public class Validator {

    public static boolean validate(Question question) {
        return question.getAnswer() != null &&
                !question.getAnswer().isEmpty() &&
                question.getBody() != null &&
                !question.getBody().isEmpty() &&
                question.getBunchId() != null;
    }
}
