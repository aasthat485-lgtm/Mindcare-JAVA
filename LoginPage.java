package mindcare;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLayeredPane layeredPane;
    private JPanel tileGrid, loginBox, registerBox;
    public static String loggedInUser = "";

    private boolean registrationActive = false;
    private String firstName = "", lastName = "", age = "", gender = "", dob = "";

    private CardLayout registerLayout = new CardLayout();
    private JPanel registerContainer = new JPanel(registerLayout);

    public static class HealthFact {
        public String symbol, text;
        public HealthFact(String s, String t) {
            symbol = s;
            text = t;
        }
    }

    private final ArrayList<HealthFact> factList = new ArrayList<>();

    public LoginPage() {
        factList.add(new HealthFact("☀", "Exercise reduces anxiety and depression."));
        factList.add(new HealthFact("☾", "Sleep improves emotional regulation."));
        factList.add(new HealthFact("♥", "Social connection boosts happiness."));
        factList.add(new HealthFact("≈", "Deep breathing relaxes your body."));
        factList.add(new HealthFact("☘", "Nature reduces cortisol levels."));
        factList.add(new HealthFact("✎", "Journaling improves clarity."));
        factList.add(new HealthFact("🕉", "Meditation reduces stress."));
        factList.add(new HealthFact("☎", "Talking helps reduce stress."));
        factList.add(new HealthFact("☕", "Hydration aids cognition."));
        factList.add(new HealthFact("♫", "Music affects mood."));
        factList.add(new HealthFact("★", "Affirmations reshape thinking."));
        factList.add(new HealthFact("⌚", "Routine gives stability."));

        setTitle("MindCare Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        tileGrid = new JPanel(new GridLayout(3, 4, 25, 25)) {
            private float offset = 0;
            private Timer timer = new Timer(20, e -> {
                // MODIFIED: Animation continues even during registration
                offset += 0.008f;
                repaint();
            });
            { timer.start(); }

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                int w = getWidth(), h = getHeight();
                float xOffset = (float) (Math.sin(offset) * w * 0.7);
                LinearGradientPaint paint = new LinearGradientPaint(
                        new Point2D.Float(xOffset, 0),
                        new Point2D.Float(xOffset + w, h),
                        new float[]{0, 0.3f, 0.5f, 0.7f, 1},
                        new Color[]{new Color(15,15,25), Color.PINK, Color.ORANGE, Color.CYAN, new Color(15,15,25)},
                        MultipleGradientPaint.CycleMethod.REPEAT
                );
                g2.setPaint(paint);
                g2.fillRect(0, 0, w, h);
            }
        };

        tileGrid.setOpaque(false);
        tileGrid.setBorder(new EmptyBorder(40, 40, 40, 40));

        String[] colors = {"blue", "green", "red", "yellow"};
        ArrayList<String> list = new ArrayList<>();
        for (String c : colors) for (int i = 0; i < 3; i++) list.add(c);
        Collections.shuffle(list);
        Collections.shuffle(factList);

        for (int i = 0; i < 12; i++) tileGrid.add(createTile(list.get(i), factList.get(i)));

        loginBox = createLoginBox();
        registerBox = createRegisterBox();

        layeredPane.add(tileGrid, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(loginBox, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(registerBox, JLayeredPane.PALETTE_LAYER);

        registerBox.setVisible(false);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                tileGrid.setBounds(0, 0, getWidth(), getHeight());
                loginBox.setBounds((getWidth()-400)/2, (getHeight()-520)/2, 400, 520);
                registerBox.setBounds((getWidth()-400)/2, (getHeight()-520)/2, 400, 520);
            }
        });
        setVisible(true);
    }

    private JPanel createTile(String colorName, HealthFact fact) {
        Color start, end;
        switch (colorName) {
            case "red": start = new Color(255,65,54); end = new Color(133,20,75); break;
            case "yellow": start = new Color(255,220,0); end = new Color(255,133,27); break;
            case "blue": start = new Color(0,116,217); end = new Color(0,31,63); break;
            case "green": start = new Color(46,204,64); end = new Color(1,150,64); break;
            default: start = Color.GRAY; end = Color.DARK_GRAY;
        }

        return new JPanel() {
            private float alpha = 0;
            private Timer t;
            
            { setOpaque(false);
              addMouseListener(new MouseAdapter() {
                  // MODIFIED: Hover facts work even during registration
                  public void mouseEntered(MouseEvent e) { fade(true); }
                  public void mouseExited(MouseEvent e) { fade(false); }
              });
            }
            void fade(boolean in) {
                if (t != null) t.stop();
                t = new Timer(20, e -> {
                    alpha += in ? 0.1f : -0.1f;
                    alpha = Math.max(0, Math.min(1, alpha));
                    repaint();
                });
                t.start();
            }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, start, getWidth(), getHeight(), end));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI Symbol", Font.BOLD, 50));
                FontMetrics fm = g2.getFontMetrics();
                int symX = (getWidth() - fm.stringWidth(fact.symbol)) / 2;
                int symY = getHeight() / 3;
                g2.drawString(fact.symbol, symX, symY);

                if (alpha > 0.05f) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    int factX = (getWidth() - g2.getFontMetrics().stringWidth(fact.text)) / 2;
                    g2.drawString(fact.text, factX, getHeight() / 3 + 50);
                }
            }
        };
    }

    private JPanel createLoginBox() {
        JPanel box = blackBox();
        GridBagConstraints gbc = baseGBC();

        JLabel title = new JLabel("MindCare Login", SwingConstants.CENTER);
        styleTitle(title, 32);
        gbc.gridwidth = 2; gbc.gridy = 0; box.add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; box.add(label("Username:"), gbc);
        usernameField = new JTextField(15);
        gbc.gridx = 1; box.add(usernameField, gbc);

        gbc.gridy = 2; gbc.gridx = 0; box.add(label("Password:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1; box.add(passwordField, gbc);

        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setForeground(Color.WHITE);
        showPass.setOpaque(false);
        showPass.addActionListener(e -> {
            if (showPass.isSelected()) passwordField.setEchoChar((char) 0);
            else passwordField.setEchoChar('•');
        });
        gbc.gridy = 3; gbc.gridx = 1; box.add(showPass, gbc);

        JButton login = new JButton("Login");
        login.addActionListener(e -> validateLogin());
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; box.add(login, gbc);

        JLabel reg = new JLabel("Don't have an account? Register here", SwingConstants.CENTER);
        reg.setForeground(Color.CYAN);
        reg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reg.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                registrationActive = true;
                firstName = ""; lastName = ""; age = "";
                registerContainer.removeAll();
                registerContainer.add(createStep1(), "STEP1");
                registerContainer.add(createStep2(), "STEP2");
                registerLayout.show(registerContainer, "STEP1");
                loginBox.setVisible(false);
                registerBox.setVisible(true);
            }
        });
        gbc.gridy = 5; box.add(reg, gbc);
        return box;
    }

    private JPanel createRegisterBox() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(registerContainer);
        return wrapper;
    }

    private JPanel createStep1() {
        JPanel box = blackBox();
        GridBagConstraints gbc = baseGBC();

        JTextField fName = new JTextField(15);
        JTextField lName = new JTextField(15);
        JTextField ageF = new JTextField(15);
        JSpinner dobSpinner = new JSpinner(new SpinnerDateModel());
        dobSpinner.setEditor(new JSpinner.DateEditor(dobSpinner, "dd/MM/yyyy"));
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male","Female","Other"});

        gbc.gridwidth = 2; gbc.gridy = 0;
        JLabel t = new JLabel("Create Profile", SwingConstants.CENTER);
        styleTitle(t, 26); box.add(t, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1; box.add(label("First Name"), gbc); gbc.gridx = 1; box.add(fName, gbc);
        gbc.gridy++; gbc.gridx = 0; box.add(label("Last Name"), gbc); gbc.gridx = 1; box.add(lName, gbc);
        gbc.gridy++; gbc.gridx = 0; box.add(label("Age"), gbc); gbc.gridx = 1; box.add(ageF, gbc);
        gbc.gridy++; gbc.gridx = 0; box.add(label("DOB"), gbc); gbc.gridx = 1; box.add(dobSpinner, gbc);
        gbc.gridy++; gbc.gridx = 0; box.add(label("Gender"), gbc); gbc.gridx = 1; box.add(genderBox, gbc);

        JButton next = new JButton("Next");
        next.addActionListener(e -> {
            String fn = fName.getText().trim();
            String ln = lName.getText().trim();
            String ag = ageF.getText().trim();

            if(fn.isEmpty() || ln.isEmpty() || ag.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are mandatory!");
                return;
            }
            if(!fn.matches("[a-zA-Z]+") || !ln.matches("[a-zA-Z]+")) {
                JOptionPane.showMessageDialog(this, "Names must contain only characters!");
                return;
            }
            try { Integer.parseInt(ag); } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Age must be an integer!");
                return;
            }

            firstName = fn; lastName = ln; age = ag;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dob = sdf.format((Date) dobSpinner.getValue());
            gender = genderBox.getSelectedItem().toString();
            registerLayout.show(registerContainer, "STEP2");
        });

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2; box.add(next, gbc);
        return box;
    }

    private JPanel createStep2() {
        JPanel box = blackBox();
        GridBagConstraints gbc = baseGBC();
        JTextField userF = new JTextField(15);
        JPasswordField passF = new JPasswordField(15);

        gbc.gridwidth = 2; gbc.gridy = 0;
        JLabel t = new JLabel("Create Credentials", SwingConstants.CENTER);
        styleTitle(t, 26); box.add(t, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1; box.add(label("Username"), gbc); gbc.gridx = 1; box.add(userF, gbc);
        gbc.gridy++; gbc.gridx = 0; box.add(label("Password"), gbc); gbc.gridx = 1; box.add(passF, gbc);

        JCheckBox showPassReg = new JCheckBox("Show Password");
        showPassReg.setForeground(Color.WHITE);
        showPassReg.setOpaque(false);
        showPassReg.addActionListener(e -> {
            if (showPassReg.isSelected()) passF.setEchoChar((char) 0);
            else passF.setEchoChar('•');
        });
        gbc.gridy++; gbc.gridx = 1; box.add(showPassReg, gbc);

        JButton finish = new JButton("Register");
        finish.addActionListener(e -> {
            String u = userF.getText().trim();
            String p = new String(passF.getPassword());

            if(u.isEmpty() || p.isEmpty()){
                JOptionPane.showMessageDialog(this, "Enter your username and password!");
                return;
            }

            boolean hasUpper = !p.equals(p.toLowerCase());
            boolean hasLower = !p.equals(p.toUpperCase());
            boolean hasDigit = p.matches(".*\\d.*");
            boolean hasSpecial = p.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

            if(p.length() < 5 || p.length() > 15 || !hasUpper || !hasLower || !hasDigit || !hasSpecial) {
                JOptionPane.showMessageDialog(this, "Password must be 5-15 chars, with 1 Upper, 1 Lower, 1 Number, and 1 Special char!");
                return;
            }

            boolean success = UserDatabase.addUser(firstName, lastName, age, gender, dob, u, p);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                registrationActive = false;
                registerBox.setVisible(false);
                loginBox.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Database error! Registration failed.");
            }
        });

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2; box.add(finish, gbc);
        return box;
    }

    private JPanel blackBox() {
        // MODIFIED: registerContainer is set to non-opaque to prevent white background leakage
        registerContainer.setOpaque(false);
        
        JPanel panel = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30,30,30,230));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),40,40);
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    private GridBagConstraints baseGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,12,10,12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        return l;
    }

    private void styleTitle(JLabel l, int size) {
        l.setFont(new Font("Segoe UI", Font.BOLD, size));
        l.setForeground(Color.WHITE);
    }

    private void validateLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        
        if (UserDatabase.checkLogin(user, pass)) {
            loggedInUser = user;
            JOptionPane.showMessageDialog(this, "Welcome " + user);
            dispose();
            new MindCareGUI();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect credentials or user does not exist.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}