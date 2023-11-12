import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(ActionListener listener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Use BoxLayout

        // Add some space at the top
        add(Box.createRigidArea(new Dimension(0, 50)));

        JButton humanVsHumanButton = new JButton("Human vs Human");
        humanVsHumanButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        humanVsHumanButton.setActionCommand("HUMAN_HUMAN");
        humanVsHumanButton.addActionListener(listener);

        JButton humanVsAIButton = new JButton("Human vs AI");
        humanVsAIButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        humanVsAIButton.setActionCommand("HUMAN_AI");
        humanVsAIButton.addActionListener(listener);

        add(humanVsHumanButton);
        add(Box.createRigidArea(new Dimension(0, 25))); // Add space between buttons
        add(humanVsAIButton);

        // Add some space at the bottom
        add(Box.createRigidArea(new Dimension(0, 50)));
    }
}

