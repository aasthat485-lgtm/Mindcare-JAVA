import javax.swing.*;
import java.awt.*;

public class CounselingPage extends JFrame {
    public CounselingPage() {
        setTitle("MindCare - Professional Support");
        // Set to 550x450 as per the target image proportions
        setSize(550, 450); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        // Main panel with NO gaps to allow the button to touch the edges
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE);

        // Header Section - Centered Title
        JLabel title = new JLabel("Talk to a Professional", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(51, 51, 51));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        // Content Section - Using a JTextArea for multi-line support
        JTextArea info = new JTextArea();
        info.setText("It's okay to not be okay. If you've been feeling low for several days, talking to a counselor can help.\n\n"
                + "Available Counselors:\n"
                + "1. Dr. Arpit (Specialist in Anxiety)\n"
                + "2. Sarah Jenkins (Behavioral Therapist)\n"
                + "3. Dr. Elena Rodriguez (Stress Management Expert)\n\n" // Added third counselor
                + "Contact: support@mindcare.com");
        
        info.setEditable(false);
        info.setFocusable(false); 
        info.setFont(new Font("Arial", Font.PLAIN, 18));
        info.setForeground(new Color(60, 60, 60));
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setBackground(Color.WHITE);
        // Padding: Top=10, Left=45, Bottom=20, Right=45 to match image margins
        info.setBorder(BorderFactory.createEmptyBorder(10, 45, 20, 45));

        // Bottom Button - Styled as a flat, full-width blue bar
        JButton callBtn = new JButton("Book a Session Now");
        callBtn.setPreferredSize(new Dimension(getWidth(), 60));
        callBtn.setBackground(new Color(125, 190, 255)); // Target Blue
        callBtn.setForeground(Color.WHITE);
        callBtn.setFont(new Font("Arial", Font.BOLD, 18));
        
        // CRITICAL: Remove all borders and painting artifacts for the "flat" look
        callBtn.setFocusPainted(false);
        callBtn.setBorderPainted(false);
        callBtn.setContentAreaFilled(true);
        callBtn.setBorder(null);
        callBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(info, BorderLayout.CENTER);
        mainPanel.add(callBtn, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new CounselingPage();
    }
}