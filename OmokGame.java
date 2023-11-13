import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class OmokGame extends JFrame implements GameStatusListener {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private BoardPanel boardPanel;
    private JLabel statusLabel;
    private AIPlayer aiPlayer;

    public OmokGame() {
        setTitle("Omok Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        mainMenuSetup();

        pack();
        setMinimumSize(new Dimension(800, 800));
        setVisible(true);

        boardPanel = new BoardPanel();
        boardPanel.setStatusListener(this);
        boardPanel.setPreferredSize(new Dimension(1000,800));

        statusLabel = new JLabel("Welcome to Omok!");
        add(statusLabel, BorderLayout.NORTH);

        setJMenuBar(createMenuBar());

        add(cardPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private void mainMenuSetup() {
        MainMenuPanel mainMenuPanel = new MainMenuPanel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                if ("HUMAN_HUMAN".equals(command)) {
                    startHumanVsHuman();
                } else if ("HUMAN_AI".equals(command)) {
                    startHumanVsAI();
                }
            }
        });
        mainMenuPanel.setPreferredSize(new Dimension(1000,800));
        cardPanel.add(mainMenuPanel, "MAIN_MENU");
        cardLayout.show(cardPanel, "MAIN_MENU");
    }

    private void startHumanVsHuman() {
        boardPanel.setGameMode(BoardPanel.GameMode.HUMAN_HUMAN);
        cardPanel.add(boardPanel, "BOARD");
        cardLayout.show(cardPanel, "BOARD");
        statusLabel.setText("Human vs Human - Black's turn.");
    }

    private void startHumanVsAI() {
        // You need to replace these with the actual values
        int boardSize = BoardPanel.BOARD_SIZE; // Assuming BOARD_SIZE is accessible
        int aiPlayerNumber = 1; // Assuming AI plays as white (1)

        aiPlayer = new AIPlayer(boardSize, aiPlayerNumber);
        boardPanel.setGameMode(BoardPanel.GameMode.HUMAN_AI);
        boardPanel.setAIPlayer(aiPlayer); // Pass the AIPlayer instance to the BoardPanel
        cardPanel.add(boardPanel, "BOARD");
        cardLayout.show(cardPanel, "BOARD");
        statusLabel.setText("Human vs AI - Your turn.");
    }


    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Menu");
        gameMenu.setMnemonic(KeyEvent.VK_G);

        JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.setMnemonic(KeyEvent.VK_N);
        newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newGameMenuItem.addActionListener(e -> resetGame());

        JMenuItem backMenuItem = new JMenuItem("Back to Menu");
        backMenuItem.addActionListener(e -> returnToMainMenu());

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        exitMenuItem.addActionListener(e -> System.exit(0));

        gameMenu.add(newGameMenuItem);
        gameMenu.add(backMenuItem); // Add the Back to Menu item here
        gameMenu.addSeparator();
        gameMenu.add(exitMenuItem);
        menuBar.add(gameMenu);

        return menuBar;
    }
    private void returnToMainMenu() {
        cardLayout.show(cardPanel, "MAIN_MENU");
        statusLabel.setText("Welcome to Omok!");
    }

    private void resetGame() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to start a new game?",
                "New Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            boardPanel.resetBoard();
            statusLabel.setText(boardPanel.getGameMode() == BoardPanel.GameMode.HUMAN_HUMAN
                    ? "Human vs Human - Black's turn." : "Human vs AI - Your turn.");
        }
    }

    @Override
    public void onStatusUpdate(String status) {
        statusLabel.setText(status);
    }
}
