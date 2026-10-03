import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OnlineExam extends JFrame {
    private final ArrayList<Question> questions = new ArrayList<>();
    private final int[] answers;

    private final JLabel questionLabel = new JLabel();
    private final JLabel progressLabel = new JLabel();
    private final JLabel timerLabel = new JLabel();
    private final JRadioButton[] optionButtons = new JRadioButton[4];
    private final ButtonGroup optionGroup = new ButtonGroup();

    private int currentQuestion = 0;
    private int secondsRemaining = 60;
    private boolean submitted = false;
    private final Timer timer;

    public OnlineExam() {
        loadQuestions();
        answers = new int[questions.size()];

        // -1 means the question has not been answered.
        for (int i = 0; i < answers.length; i++) {
            answers[i] = -1;
        }

        buildInterface();
        showQuestion();

        timer = new Timer(1000, event -> {
            secondsRemaining--;
            updateTimer();

            if (secondsRemaining <= 0) {
                submitExam();
            }
        });
        timer.start();
    }

    private void buildInterface() {
        setTitle("Online Examination System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JPanel header = new JPanel(new BorderLayout());
        progressLabel.setFont(new Font("Arial", Font.BOLD, 15));
        timerLabel.setFont(new Font("Arial", Font.BOLD, 15));
        header.add(progressLabel, BorderLayout.WEST);
        header.add(timerLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        questionLabel.setFont(new Font("Arial", Font.BOLD, 17));
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(18));

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 15));
            optionGroup.add(optionButtons[i]);
            centerPanel.add(optionButtons[i]);
        }

        add(centerPanel, BorderLayout.CENTER);

        JButton previousButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        JButton submitButton = new JButton("Submit");

        previousButton.addActionListener(event -> {
            saveAnswer();
            if (currentQuestion > 0) {
                currentQuestion--;
                showQuestion();
            }
        });

        nextButton.addActionListener(event -> {
            saveAnswer();
            if (currentQuestion < questions.size() - 1) {
                currentQuestion++;
                showQuestion();
            }
        });

        submitButton.addActionListener(event -> submitExam());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
    }

    private void showQuestion() {
        Question question = questions.get(currentQuestion);

        progressLabel.setText(
                "Question " + (currentQuestion + 1) + " of " + questions.size()
        );
        questionLabel.setText("<html>" + (currentQuestion + 1) + ". "
                + question.getText() + "</html>");

        String[] options = question.getOptions();
        optionGroup.clearSelection();

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options[i]);
            optionButtons[i].setSelected(answers[currentQuestion] == i);
        }
    }

    private void saveAnswer() {
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                answers[currentQuestion] = i;
                return;
            }
        }
        answers[currentQuestion] = -1;
    }

    private void updateTimer() {
        timerLabel.setText("Time remaining: " + secondsRemaining + " seconds");
    }

    private void submitExam() {
        if (submitted) {
            return;
        }

        saveAnswer();
        submitted = true;
        timer.stop();

        int correct = 0;
        int wrong = 0;

        for (int i = 0; i < questions.size(); i++) {
            if (answers[i] == -1) {
                continue; // Unanswered questions are not counted as wrong.
            }

            if (questions.get(i).isCorrect(answers[i])) {
                correct++;
            } else {
                wrong++;
            }
        }

        int total = questions.size();
        double percentage = (correct * 100.0) / total;
        String status = percentage >= 40.0 ? "PASS" : "FAIL";

        JOptionPane.showMessageDialog(
                this,
                String.format(
                        "Total questions: %d%nCorrect answers: %d%n"
                                + "Wrong answers: %d%nUnanswered: %d%n"
                                + "Percentage: %.2f%%%nResult: %s",
                        total, correct, wrong, total - correct - wrong,
                        percentage, status
                ),
                "Exam Result",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
    }

    private void loadQuestions() {
        questions.add(new Question("What is the capital of France?",
                new String[]{"Berlin", "Madrid", "Paris", "Rome"}, 2));
        questions.add(new Question("Which planet is known as the Red Planet?",
                new String[]{"Venus", "Mars", "Jupiter", "Mercury"}, 1));
        questions.add(new Question("What is 5 + 7?",
                new String[]{"10", "11", "12", "13"}, 2));
        questions.add(new Question("Which language runs on the Java Virtual Machine?",
                new String[]{"Java", "HTML", "CSS", "SQL"}, 0));
        questions.add(new Question("What is the largest ocean?",
                new String[]{"Atlantic", "Indian", "Arctic", "Pacific"}, 3));
        questions.add(new Question("How many sides does a hexagon have?",
                new String[]{"Five", "Six", "Seven", "Eight"}, 1));
        questions.add(new Question("Which data structure uses FIFO order?",
                new String[]{"Stack", "Queue", "Tree", "Graph"}, 1));
        questions.add(new Question("What does CPU stand for?",
                new String[]{"Central Processing Unit", "Computer Personal Unit",
                        "Central Program Utility", "Control Processing User"}, 0));
        questions.add(new Question("Which gas do plants absorb?",
                new String[]{"Oxygen", "Nitrogen", "Carbon dioxide", "Hydrogen"}, 2));
        questions.add(new Question("What is the result of 3 * 4?",
                new String[]{"7", "10", "12", "14"}, 2));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OnlineExam().setVisible(true));
    }
}
