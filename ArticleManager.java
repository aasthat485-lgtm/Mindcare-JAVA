package mindcare; // Add this so it can talk to your other files

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class ArticleManager extends JDialog {
    private Color primaryBlue = new Color(74, 144, 226);

    public ArticleManager(JFrame parent) {
        super(parent, "Wellness Articles", true); 
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        // Add your articles here
        listPanel.add(createArticleCard("The Power of Vulnerability", "https://www.ted.com"));
        listPanel.add(createArticleCard("Managing Stress", "https://www.healthline.com"));

        add(new JScrollPane(listPanel), BorderLayout.CENTER);
        // Note: Do not put setVisible(true) inside the constructor 
        // if you want to call it from MindCareGUI smoothly.
    }

    private JPanel createArticleCard(String title, String link) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setBackground(Color.WHITE);
        card.add(new JLabel(title), BorderLayout.CENTER);

        JButton viewBtn = new JButton("View");
        viewBtn.addActionListener(e -> {
            try { 
                Desktop.getDesktop().browse(new URI(link)); 
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        card.add(viewBtn, BorderLayout.EAST);
        return card;
    }
}