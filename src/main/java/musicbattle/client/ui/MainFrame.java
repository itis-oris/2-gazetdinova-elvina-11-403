package musicbattle.client.ui;

import javax.swing.*;

public class MainFrame extends JFrame {

    private static MainFrame instance;

    public MainFrame() {
        instance = this;

        setTitle("Music Battle 💖");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showConnectPanel();

        setVisible(true);
    }

    public static MainFrame getInstance() {
        return instance;
    }

    public void showConnectPanel() {
        setContentPane(new ConnectPanel());
        revalidate();
        repaint();
    }

    public void showGamePanel() {
        setContentPane(new GamePanel());
        revalidate();
        repaint();
    }
}
