package mindcare;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MindCareGUI extends JFrame {
    JTextField idField, nameField, noteField;
    JComboBox<String> moodField;
    JButton addBtn, updateBtn, deleteBtn, clearBtn, trendBtn, counselBtn, profileBtn, articleBtn; // Added articleBtn

    JPanel rightContentPanel;
    JScrollPane tableScroll;
    JPanel emptyStatePanel;
    JTable table;

    DefaultTableModel model;
    ArrayList<Mood> moodList = new ArrayList<>();

    Color bgColor = new Color(240, 244, 248);
    Color primaryBlue = new Color(74, 144, 226);
    Color hoverBlue = new Color(53, 122, 189);

    public MindCareGUI() {
        setTitle("MindCare - Mental Health Tracker");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String currentUser = LoginPage.loggedInUser;
        
        // Fetch details from Database instead of HashMaps
        String[] userDetails = UserDatabase.getUserDetails(currentUser);
        String firstName = (userDetails != null) ? userDetails[0] : "User";

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        // ===== HEADER SECTION =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryBlue);
        headerPanel.setPreferredSize(new Dimension(1000, 100));

        JPanel leftHeaderWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 17));
        leftHeaderWrapper.setOpaque(false);

        JLabel logoLabel = new JLabel();
        try {
            String logoPath = "C:\\Users\\vansh\\Downloads\\Gemini_Generated_Image_qvssctqvssctqvss.png";
            ImageIcon logoIcon = new ImageIcon(logoPath);
            Image logoImg = logoIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(logoImg));
        } catch (Exception e) {
            logoLabel.setText("🧠");
            logoLabel.setForeground(Color.WHITE);
        }

        profileBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int w = getWidth();
                int h = getHeight();
                int lineW = 22; 
                int x = (w - lineW) / 2;
                g2.drawLine(x, h/2 - 8, x + lineW, h/2 - 8);
                g2.drawLine(x, h/2,     x + lineW, h/2);
                g2.drawLine(x, h/2 + 8, x + lineW, h/2 + 8);
                g2.dispose();
            }
        };
        profileBtn.setOpaque(false);
        profileBtn.setContentAreaFilled(false);
        profileBtn.setBorder(null);
        profileBtn.setFocusPainted(false);
        profileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profileBtn.setPreferredSize(new Dimension(50, 50));

        leftHeaderWrapper.add(logoLabel);
        leftHeaderWrapper.add(profileBtn);

        JLabel title = new JLabel("MindCare Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Hello " + firstName + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));

        headerPanel.add(leftHeaderWrapper, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(welcomeLabel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ===== SIDEBAR FORM =====
        JPanel sideBar = new JPanel(new GridBagLayout());
        sideBar.setBackground(Color.WHITE);
        sideBar.setPreferredSize(new Dimension(350, 570));
        sideBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 5, 0);

        JLabel formTitle = new JLabel("Log Today's Mood");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sideBar.add(formTitle, gbc);

        idField = createStyledTextField();
        nameField = createStyledTextField();
        nameField.setText(firstName); 
        noteField = createStyledTextField();

        moodField = new JComboBox<>(new String[]{
                "😊 Happy", "😔 Sad", "😡 Anger", "😨 Fear", "😲 Surprised", "😓 Stressed", "✍ Other"
        });
        moodField.setPreferredSize(new Dimension(250, 40));

        addLabelToSideBar(sideBar, "Entry ID", gbc);
        sideBar.add(idField, gbc);
        addLabelToSideBar(sideBar, "Name", gbc);
        sideBar.add(nameField, gbc);
        addLabelToSideBar(sideBar, "Mood", gbc);
        sideBar.add(moodField, gbc);
        addLabelToSideBar(sideBar, "Note", gbc);
        sideBar.add(noteField, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        sideBar.add(Box.createVerticalGlue(), gbc);
        gbc.weighty = 0; gbc.gridy++;

        JPanel btnPanel = new JPanel(new GridLayout(0, 2, 12, 12)); // Updated to 0 rows for auto-expansion
        btnPanel.setBackground(Color.WHITE);

        addBtn = new RoundedButton("Add", primaryBlue, hoverBlue);
        updateBtn = new RoundedButton("Update", primaryBlue, hoverBlue);
        deleteBtn = new RoundedButton("Delete", primaryBlue, hoverBlue);
        clearBtn = new RoundedButton("Clear", primaryBlue, hoverBlue);
        trendBtn = new RoundedButton("Trends Dashboard", primaryBlue, hoverBlue);
        counselBtn = new RoundedButton("Visit Counselor", primaryBlue, hoverBlue);
        articleBtn = new RoundedButton("Wellness Articles", primaryBlue, hoverBlue); // New Button

        btnPanel.add(addBtn); btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn); btnPanel.add(clearBtn);
        btnPanel.add(trendBtn); btnPanel.add(counselBtn);
        btnPanel.add(articleBtn); // Added to panel

        sideBar.add(btnPanel, gbc);
        mainPanel.add(sideBar, BorderLayout.WEST);

        rightContentPanel = new JPanel(new CardLayout());
        emptyStatePanel = createEmptyState();
        model = new DefaultTableModel(new String[]{"ID", "Name", "Mood", "Note"}, 0);
        table = new JTable(model);
        table.setRowHeight(30);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    idField.setText(model.getValueAt(row, 0).toString());
                    nameField.setText(model.getValueAt(row, 1).toString());
                    moodField.setSelectedItem(model.getValueAt(row, 2).toString());
                    noteField.setText(model.getValueAt(row, 3).toString());
                    idField.setEditable(false);
                }
            }
        });

        tableScroll = new JScrollPane(table);
        rightContentPanel.add(emptyStatePanel, "EMPTY");
        rightContentPanel.add(tableScroll, "DATA");
        mainPanel.add(rightContentPanel, BorderLayout.CENTER);

        addBtn.addActionListener(e -> addMood());
        updateBtn.addActionListener(e -> updateMood());
        deleteBtn.addActionListener(e -> deleteMood());
        clearBtn.addActionListener(e -> clearFields());
        trendBtn.addActionListener(e -> new TrendDashboard(moodList));
        counselBtn.addActionListener(e -> new CounselingPage());
        profileBtn.addActionListener(e -> new ProfilePopup(this, currentUser, primaryBlue));
        articleBtn.addActionListener(e -> {
            new ArticleManager(this); 
        }); // Added action listener

        add(mainPanel);
        setVisible(true);
    }

    private void addMood() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String mood = moodField.getSelectedItem().toString();
            String note = noteField.getText();
            model.addRow(new Object[]{id, name, mood, note});
            moodList.add(new Mood(id, name, mood, note));
            updateViewState();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID.");
        }
    }

    private void updateMood() {
        int row = table.getSelectedRow();
        if (row != -1) {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String mood = moodField.getSelectedItem().toString();
                String note = noteField.getText();
                model.setValueAt(id, row, 0);
                model.setValueAt(name, row, 1);
                model.setValueAt(mood, row, 2);
                model.setValueAt(note, row, 3);
                moodList.set(row, new Mood(id, name, mood, note));
                clearFields();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Update failed."); }
        } else { JOptionPane.showMessageDialog(this, "Select a row to update."); }
    }

    private void deleteMood() {
        int row = table.getSelectedRow();
        if (row != -1) {
            model.removeRow(row);
            moodList.remove(row);
            updateViewState();
            clearFields();
        } else { JOptionPane.showMessageDialog(this, "Select a row to delete."); }
    }

    private void clearFields() {
        idField.setText("");
        String[] userDetails = UserDatabase.getUserDetails(LoginPage.loggedInUser);
        nameField.setText(userDetails != null ? userDetails[0] : "User");
        noteField.setText("");
        moodField.setSelectedIndex(0);
        idField.setEditable(true);
        table.clearSelection();
    }

    private void updateViewState() {
        CardLayout cl = (CardLayout) rightContentPanel.getLayout();
        cl.show(rightContentPanel, model.getRowCount() == 0 ? "EMPTY" : "DATA");
    }

    private JPanel createEmptyState() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(bgColor);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        JLabel imageLabel = new JLabel();
        try {
            String treePath = "C:\\Users\\vansh\\Downloads\\—Pngtree—beautiful spring tree illustration green_13356202.png";
            imageLabel.setIcon(new ImageIcon(new ImageIcon(treePath).getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH)));
        } catch (Exception e) { imageLabel.setText("🌳"); }
        p.add(imageLabel, c);
        c.gridy++;
        c.insets = new Insets(15, 0, 0, 0);
        JLabel quoteLabel = new JLabel("<html><div style='text-align:center;'><b>Your journey starts here.</b><br>Add your first mood entry!</div></html>");
        quoteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        quoteLabel.setForeground(new Color(120, 120, 120));
        p.add(quoteLabel, c);
        return p;
    }

    private JTextField createStyledTextField() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(250, 40));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return f;
    }

    private void addLabelToSideBar(JPanel p, String text, GridBagConstraints gbc) {
        gbc.gridy++;
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        p.add(l, gbc);
        gbc.gridy++;
    }

    class RoundedButton extends JButton {
        public RoundedButton(String text, Color base, Color hover) {
            super(text);
            setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
            setForeground(Color.WHITE); setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBackground(base);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    class ProfilePopup extends JDialog {
        private String currentUser;
        private JPopupMenu optionsMenu;

        public ProfilePopup(JFrame parent, String username, Color blueColor) {
            super(parent, false);
            this.currentUser = username;
            setUndecorated(true);
            setSize(360, 360);
            Point buttonPos = profileBtn.getLocationOnScreen();
            setLocation(buttonPos.x, buttonPos.y + profileBtn.getHeight() + 5);

            // Fetch dynamic details from UserDatabase class
            String[] details = UserDatabase.getUserDetails(username);
            String fullName = (details != null) ? (details[0] + " " + details[1]) : "Unknown User";
            String age = (details != null) ? details[2] : "N/A";
            String gender = (details != null) ? details[3] : "N/A";
            String id = "#" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12);
            String dominantMood = calculateDominantMood();

            JPanel mainP = new JPanel(new BorderLayout());
            mainP.setBackground(blueColor);
            mainP.setBorder(BorderFactory.createLineBorder(blueColor, 1));

            JPanel headerP = new JPanel(new GridBagLayout());
            headerP.setOpaque(false);
            headerP.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
            GridBagConstraints gbc = new GridBagConstraints();

            JPanel textContainer = new JPanel(new GridLayout(2, 1));
            textContainer.setOpaque(false);
            JLabel nameLabel = new JLabel(fullName.toUpperCase());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
            nameLabel.setForeground(Color.WHITE);
            JLabel idLabel = new JLabel(id);
            idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            idLabel.setForeground(new Color(230, 230, 230));
            textContainer.add(nameLabel);
            textContainer.add(idLabel);

            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            headerP.add(textContainer, gbc);

            JLayeredPane photoLayer = new JLayeredPane();
            photoLayer.setPreferredSize(new Dimension(50, 50));

            JPanel imagePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(220, 220, 220));
                    int size = 50;
                    Ellipse2D.Double outerCircle = new Ellipse2D.Double(0, 0, size, size);
                    g2.fill(outerCircle);
                    g2.setColor(Color.WHITE);
                    double hS = size * 0.40, hX = (size - hS) / 2, hY = size * 0.20;
                    double bW = size * 0.70, bH = size * 0.35, bX = (size - bW) / 2, bY = size * 0.65;
                    Area combined = new Area(new Ellipse2D.Double(hX, hY, hS, hS));
                    combined.add(new Area(new Ellipse2D.Double(bX, bY, bW, bH)));
                    Area finalArea = new Area(outerCircle);
                    finalArea.intersect(combined);
                    g2.fill(finalArea);
                    g2.dispose();
                }
            };
            imagePanel.setBounds(0, 0, 50, 50);
            imagePanel.setOpaque(false);

            JButton pencilBtn = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.WHITE);
                    g2.fillOval(0, 0, 20, 20); 
                    g2.setColor(new Color(74, 144, 226)); 
                    g2.setStroke(new BasicStroke(2.0f));
                    g2.drawRect(5, 5, 8, 8); 
                    g2.drawLine(11, 5, 15, 1);
                    g2.dispose();
                }
            };
            pencilBtn.setBounds(32, -2, 20, 20);
            pencilBtn.setOpaque(false); pencilBtn.setContentAreaFilled(false); pencilBtn.setBorder(null);
            pencilBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            optionsMenu = new JPopupMenu();
            JMenuItem takeItem = new JMenuItem("Take Photo");
            takeItem.setFont(new Font("Segoe UI", Font.PLAIN, 12)); 
            JMenuItem chooseItem = new JMenuItem("Choose from Gallery");
            chooseItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            optionsMenu.add(takeItem);
            optionsMenu.add(chooseItem);

            pencilBtn.addActionListener(e -> optionsMenu.show(pencilBtn, 0, pencilBtn.getHeight()));

            photoLayer.add(imagePanel, JLayeredPane.DEFAULT_LAYER);
            photoLayer.add(pencilBtn, JLayeredPane.PALETTE_LAYER);

            gbc.gridx = 1; gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
            headerP.add(photoLayer, gbc);

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setOpaque(false);
            
            JSeparator line = new JSeparator();
            line.setForeground(new Color(255, 255, 255, 80));
            line.setBackground(new Color(255, 255, 255, 80));
            
            JPanel detailsP = new JPanel(new GridLayout(4, 1, 0, 10));
            detailsP.setOpaque(false);
            detailsP.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

            addDetailRow(detailsP, "Username: ", username);
            addDetailRow(detailsP, "Age: ", age);
            addDetailRow(detailsP, "Gender: ", gender);
            addDetailRow(detailsP, "Dominant Mood: ", dominantMood);

            centerPanel.add(line, BorderLayout.NORTH);
            centerPanel.add(detailsP, BorderLayout.CENTER);

            mainP.add(headerP, BorderLayout.NORTH);
            mainP.add(centerPanel, BorderLayout.CENTER);

            this.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
                public void windowLostFocus(java.awt.event.WindowEvent e) { 
                    if (optionsMenu != null && !optionsMenu.isVisible()) dispose(); 
                }
                public void windowGainedFocus(java.awt.event.WindowEvent e) {}
            });

            add(mainP);
            setVisible(true);
        }

        private void addDetailRow(JPanel p, String title, String value) {
            JLabel l = new JLabel(title + value);
            l.setFont(new Font("Segoe UI", Font.BOLD, 18));
            l.setForeground(Color.WHITE);
            p.add(l);
        }

        private String calculateDominantMood() {
            if (moodList.isEmpty()) return "None yet";
            Map<String, Integer> counts = new HashMap<>();
            for (Mood m : moodList) {
                counts.put(m.getMood(), counts.getOrDefault(m.getMood(), 0) + 1);
            }
            String dominant = "None yet";
            int max = -1;
            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    dominant = entry.getKey();
                }
            }
            return dominant;
        }
    }

    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> new MindCareGUI()); 
    }
}