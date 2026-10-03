public class Question {
    private final String text;
    private final String[] options;
    private final int correctAnswerIndex;

    public Question(String text, String[] options, int correctAnswerIndex) {
        if (options == null || options.length != 4) {
            throw new IllegalArgumentException("Each question must have four options.");
        }
        if (correctAnswerIndex < 0 || correctAnswerIndex >= options.length) {
            throw new IllegalArgumentException("Invalid correct answer index.");
        }

        this.text = text;
        this.options = options.clone();
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options.clone();
    }

    public boolean isCorrect(int selectedAnswerIndex) {
        return selectedAnswerIndex == correctAnswerIndex;
    }
}
