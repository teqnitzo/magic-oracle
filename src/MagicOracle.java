import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MagicOracle extends JFrame {
    private final JTextField questionField;
    private final Locale currentLocale;
    private String askButtonText;
    private String errorMessage;
    private String answerTitle;
    private String correctQuestionMessage;

    public MagicOracle(Locale locale) {
        this.currentLocale = locale;
        setLanguage(locale);

        setTitle("Magic Oracle / Магический Оракул");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель для ввода вопроса
        JPanel inputPanel = new JPanel();
        questionField = new JTextField(25); // Длина поля
        inputPanel.add(questionField);
        add(inputPanel, BorderLayout.NORTH);

        // Панель для кнопки (по центру)
        JPanel buttonPanel = new JPanel();
        JButton askButton = new JButton(askButtonText);
        askButton.setPreferredSize(new Dimension(80, 30)); // Компактная кнопка
        buttonPanel.add(askButton);
        add(buttonPanel, BorderLayout.CENTER);

        // Обработчик кнопки
        askButton.addActionListener(_ -> {
            String question = questionField.getText().trim();
            if (question.endsWith("?")) {
                String answer = getDeterministicAnswer(question);
                JOptionPane.showMessageDialog(MagicOracle.this, answer, answerTitle, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(MagicOracle.this, correctQuestionMessage, errorMessage, JOptionPane.WARNING_MESSAGE);
            }
        });

        setVisible(true);
        setLocationRelativeTo(null); // Центрируем окно на экране
    }

    private void setLanguage(Locale locale) {
        if (locale.getLanguage().equals("ru")) {
            askButtonText = "Ответ";
            errorMessage = "Ошибка";
            answerTitle = "Ответ";
            correctQuestionMessage = "Введите корректный вопрос";
        } else {
            askButtonText = "Ask";
            errorMessage = "Error";
            answerTitle = "Answer";
            correctQuestionMessage = "Please enter a valid question";
        }
    }

    private String getDeterministicAnswer(String question) {
        try {
            // Используем хеш-функцию MD5 для получения уникального хеша вопроса
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(question.getBytes(StandardCharsets.UTF_8));

            // Берем первый байт и приводим его к 0 или 1
            int result = (hash[0] & 1);
            if (currentLocale.getLanguage().equals("ru")) {
                return result == 0 ? "Да" : "Нет";
            } else {
                return result == 0 ? "Yes" : "No";
            }
        } catch (NoSuchAlgorithmException e) {
            return "Error"; // В маловероятном случае ошибки
        }
    }

    public static void main(String[] args) {
        String[] options = {"Русский", "English"};
        int choice = JOptionPane.showOptionDialog(null, "Выберите язык / Choose a language:",
                "Language Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        Locale selectedLocale = (choice == 0) ? Locale.of("ru") : Locale.of("en");
        new MagicOracle(selectedLocale);
    }
}
