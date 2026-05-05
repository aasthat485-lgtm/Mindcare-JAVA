package mindcare;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RegistrationPage extends JFrame {
    private JPanel cards;
    private CardLayout cl;
    private JTextField firstNameField, lastNameField, ageField, userField;
    private JPasswordField passField;
    private JComboBox<String> genderBox;

    public static String registeredUser = "";
    public static String registeredPass = "";

    public RegistrationPage() {
        setTitle("MindCare - User Registration");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cl = new CardLayout();
        cards = new JPanel(cl);
        initStep1();
        initStep2();
        add(cards);
        setVisible(true);
    }

    private void initStep1() {
        JPanel p1 = new JPanel(new GridBagLayout());
        p1.setBackground(new Color(240, 244, 248));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Step 1: Create Profile", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        ageField = new JTextField(15);
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JButton nextBtn = new JButton("Next Step");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; p1.add(title, gbc);
        gbc.gridwidth = 1; 
        gbc.gridy = 1; gbc.gridx = 0; p1.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1; p1.add(firstNameField, gbc);
        gbc.gridy = 2; gbc.gridx = 0; p1.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1; p1.add(lastNameField, gbc);
        gbc.gridy = 3; gbc.gridx = 0; p1.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; p1.add(ageField, gbc);
        gbc.gridy = 4; gbc.gridx = 0; p1.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1; p1.add(genderBox, gbc);
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2; p1.add(nextBtn, gbc);

        nextBtn.addActionListener(e -> { if (validateStep1()) cl.show(cards, "step2"); });
        cards.add(p1, "step1");
    }

    private boolean validateStep1() {
        String fName = firstNameField.getText().trim();
        String lName = lastNameField.getText().trim();
        String ageStr = ageField.getText().trim();
        if (fName.isEmpty() || lName.isEmpty() || ageStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are mandatory!");
            return false;
        }
        try { Integer.parseInt(ageStr); } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
            return false;
        }
        return true;
    }

    private void initStep2() {
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(new Color(240, 244, 248));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        userField = new JTextField(15);
        passField = new JPasswordField(15);
        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setBackground(new Color(240, 244, 248));
        JButton finishBtn = new JButton("Complete Registration");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel t2 = new JLabel("Step 2: Security", SwingConstants.CENTER);
        t2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        p2.add(t2, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0; p2.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; p2.add(userField, gbc);
        gbc.gridy = 2; gbc.gridx = 0; p2.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; p2.add(passField, gbc);
        gbc.gridy = 3; gbc.gridx = 1; p2.add(showPass, gbc);
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; p2.add(finishBtn, gbc);

        showPass.addActionListener(e -> passField.setEchoChar(showPass.isSelected() ? (char) 0 : '\u2022'));

        finishBtn.addActionListener(e -> validateStep2());
        cards.add(p2, "step2");
    }

    private boolean isPasswordStrong(String password) {
        if (password.length() < 8) return false;
        boolean hasUpper = !password.equals(password.toLowerCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        return hasUpper && hasDigit && hasSpecial;
    }

    private void validateStep2() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());
        
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty!");
            return;
        }

        if (!isPasswordStrong(pass)) {
            JOptionPane.showMessageDialog(this, "Password Weak!\nMust have: 8+ chars, 1 Uppercase, 1 Number, and 1 Special Character.");
            return;
        }

        registeredUser = user;
        registeredPass = pass;

        // ✅ ONLY CHANGE: removed ID display
        JOptionPane.showMessageDialog(this, "Registration Successful!");

        dispose();
        new LoginPage(); 
    }

    private String generateRandomID() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("#");
        Random rnd = new Random();
        for (int i = 0; i < 12; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}