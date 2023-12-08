package newview;
/**
 * STUDENT: MOHAMMED ALHAIDARI (HAIDARI ALHAIDARI)
 * STUDENT ID: VRL968 / 11356223
 * COURSE: DR. JASON BOWEY / CMPT 270 T01
 */

import java.awt.*;
import java.awt.event.ActionListener;
import model.*;
import javax.swing.*;
import model.GameInfoProvider;


public class EnergyLevel extends JFrame implements GameObserver {
    private GameInfoProvider gameInfo;
    private JLabel energyLabel;
    private JLabel damageLabel;
    private Timer damageTakenTimer;
    private int prevLives = 4;

    public EnergyLevel(GameInfoProvider gameInfoProvider) {
        gameInfo = gameInfoProvider;
        initComponents();
        createGUI();
    }

    private void createGUI() {
        // Set up JFrame properties
        setSize(200, 100);
        setTitle("Energy Level");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(200, 100));
        setLocation(new Point(Toolkit.getDefaultToolkit().getScreenSize().width - getWidth(), 0));

        // Create and configure JPanel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.BLACK);
        panel.add(damageLabel, BorderLayout.NORTH);
        panel.add(energyLabel, BorderLayout.SOUTH);

        // Add JPanel to JFrame
        add(panel);

        // Finalize JFrame configuration
        pack();
        setVisible(true);
    }

    private void initComponents() {
        // Set up ActionListener for damageLabel clearing
        ActionListener actionListener = e -> damageLabel.setText("");
        damageTakenTimer = new Timer(2000, actionListener);

        // Initialize GUI components
        damageLabel = new JLabel("");
        damageLabel.setForeground(Color.GREEN);
        damageLabel.setFont(new Font("Arial", Font.BOLD, 15));

        energyLabel = new JLabel("Energy Level: " + gameInfo.getEnergyLevel());
        energyLabel.setForeground(Color.GREEN);
    }

    @Override
    public void gameChanged() {
        // Update energyLabel based on game state
        if (gameInfo.isLowPowerStatus()) {
            energyLabel.setText("LOW POWER");
        } else {
            energyLabel.setText("Energy Level: " + gameInfo.getEnergyLevel());
        }

        // Display "DAMAGE TAKEN" and start the timer if lives decrease
        if (gameInfo.getPlayerLives() < prevLives) {
            prevLives--;
            damageLabel.setText("DAMAGE TAKEN");
            damageTakenTimer.start();
        }

        // Hide JFrame if the game is over
        if (gameInfo.isOver()) {
            setVisible(false);
        }

        repaint();
    }
}
