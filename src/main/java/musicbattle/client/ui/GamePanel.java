package musicbattle.client.ui;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private static JLabel scoreLabel = new JLabel("Очки: ", SwingConstants.CENTER);

    public GamePanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("🎵 Сыграй мелодию (C D E)", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        add(title, BorderLayout.NORTH);
        add(scoreLabel, BorderLayout.SOUTH);
        add(new musicbattle.client.ui.piano.PianoPanel(), BorderLayout.CENTER);
    }

    public static void updateScore(String text) {
        scoreLabel.setText("Очки: " + text);
    }
}

