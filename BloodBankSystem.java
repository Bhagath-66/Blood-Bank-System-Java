import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.*;

public class BloodBankSystem extends JFrame {

    private final JPanel mainPanel;
    private final CardLayout cardLayout;

    private static final Color RED = new Color(178, 34, 52);
    private static final Color DARK_RED = new Color(118, 18, 35);
    private static final Color LIGHT_RED = new Color(247, 231, 232);
    private static final Color WHITE = Color.WHITE;
    private static final Color SOFT_TEXT = new Color(80, 80, 80);
    private static final Color PANEL_BG = new Color(250, 250, 250);

    private String loggedUser = "";
    private int loggedUserId = -1; // -1 means no user is logged in

    public BloodBankSystem() {
        setTitle("Blood Bank Management System");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPage(), "login");
        mainPanel.add(createAccountPage(), "create");
        mainPanel.add(homePage(), "home");
        mainPanel.add(donorPage(), "donor");
        mainPanel.add(receiverPage(), "receiver");
        mainPanel.add(hospitalPage(), "hospital");
        mainPanel.add(profilePage(), "profile");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    // =========================================================
    // LOGIN PAGE
    // =========================================================

    private JPanel loginPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(LIGHT_RED);

        JPanel box = new JPanel(new GridBagLayout());
        box.setBackground(WHITE);
        box.setBorder(new CompoundBorder(
                new LineBorder(RED, 2),
                new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("BLOOD BANK SYSTEM");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(RED);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("Login");
        subtitle.setFont(new Font("Arial", Font.BOLD, 22));
        subtitle.setForeground(SOFT_TEXT);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField username = new JTextField(18);
        JPasswordField password = new JPasswordField(18);
        styleTextField(username);
        styleTextField(password);

        JButton login = createPrimaryButton("Login");
        JButton create = createSecondaryButton("Create Account");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        box.add(title, gbc);

        gbc.gridy++;
        box.add(subtitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        box.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        box.add(username, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        box.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        box.add(password, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        box.add(login, gbc);

        gbc.gridx = 1;
        box.add(create, gbc);

        login.addActionListener(e -> {
            String user = username.getText().trim();
            char[] passChars = password.getPassword();
            String pass = new String(passChars);

            if (user.isEmpty() || passChars.length == 0) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.");
                return;
            }

            String sql = "SELECT id, password FROM users WHERE username = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, user);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String storedHash = rs.getString("password");
                        if (storedHash.equals(hashPassword(pass))) {
                            loggedUser = user;
                            loggedUserId = rs.getInt("id");
                            JOptionPane.showMessageDialog(this, "Login successful!");
                            username.setText("");
                            password.setText("");
                            cardLayout.show(mainPanel, "home");
                        } else {
                            JOptionPane.showMessageDialog(this, "Incorrect password.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Username not found. Please create an account.");
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        create.addActionListener(e -> cardLayout.show(mainPanel, "create"));

        panel.add(box);
        return panel;
    }

    // =========================================================
    // CREATE ACCOUNT PAGE
    // =========================================================

    private JPanel createAccountPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);
        panel.add(header("Create Account"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(PANEL_BG);
        form.setBorder(new EmptyBorder(30, 100, 30, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField username = new JTextField(20);
        JPasswordField password = new JPasswordField(20);
        JPasswordField confirm = new JPasswordField(20);
        JTextField mobile = new JTextField(20);

        styleTextField(username);
        styleTextField(password);
        styleTextField(confirm);
        styleTextField(mobile);

        addField(form, gbc, 0, "Username:", username);
        addField(form, gbc, 1, "Create Password:", password);
        addField(form, gbc, 2, "Confirm Password:", confirm);
        addField(form, gbc, 3, "Mobile No:", mobile);

        JButton create = createPrimaryButton("Create Account");
        JButton back = createSecondaryButton("Back to Login");

        gbc.gridx = 0;
        gbc.gridy = 4;
        form.add(create, gbc);

        gbc.gridx = 1;
        form.add(back, gbc);

        create.addActionListener(e -> {
            String user = username.getText().trim();
            String pass = new String(password.getPassword());
            String conf = new String(confirm.getPassword());
            String mob = mobile.getText().trim();

            if (user.isEmpty() || pass.isEmpty() || conf.isEmpty() || mob.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            if (!pass.equals(conf)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            String checkSql = "SELECT id FROM users WHERE username = ?";
            String insertSql = "INSERT INTO users (username, password, mobile) VALUES (?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection()) {

                try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                    check.setString(1, user);
                    try (ResultSet rs = check.executeQuery()) {
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(this, "Username already exists.");
                            return;
                        }
                    }
                }

                try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                    insert.setString(1, user);
                    insert.setString(2, hashPassword(pass));
                    insert.setString(3, mob);
                    insert.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Account created successfully!");
                username.setText("");
                password.setText("");
                confirm.setText("");
                mobile.setText("");
                cardLayout.show(mainPanel, "login");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        back.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // HOME PAGE
    // =========================================================

    private JPanel homePage() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);

        panel.add(header("Blood Bank Management System"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 2, 25, 25));
        center.setBackground(LIGHT_RED);
        center.setBorder(new EmptyBorder(60, 100, 60, 100));

        JButton donor = createMenuButton("DONOR");
        JButton receiver = createMenuButton("RECEIVER");
        JButton hospital = createMenuButton("HOSPITAL");
        JButton profile = createMenuButton("PROFILE");

        center.add(donor);
        center.add(receiver);
        center.add(hospital);
        center.add(profile);

        panel.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setBackground(WHITE);

        JButton logout = createSecondaryButton("Logout");
        bottom.add(logout);

        logout.addActionListener(e -> {
            loggedUser = "";
            loggedUserId = -1;
            cardLayout.show(mainPanel, "login");
        });

        panel.add(bottom, BorderLayout.SOUTH);

        donor.addActionListener(e ->
                cardLayout.show(mainPanel, "donor")
        );

        receiver.addActionListener(e ->
                cardLayout.show(mainPanel, "receiver")
        );

        hospital.addActionListener(e ->
                cardLayout.show(mainPanel, "hospital")
        );

        profile.addActionListener(e ->
                cardLayout.show(mainPanel, "profile")
        );

        return panel;
    }

    // =========================================================
    // DONOR PAGE
    // =========================================================

    private JPanel donorPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);
        panel.add(header("Donor Registration"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(PANEL_BG);
        form.setBorder(new EmptyBorder(20, 100, 20, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField name = new JTextField(20);
        JTextField mobile = new JTextField(20);
        JTextField address = new JTextField(20);
        JTextField age = new JTextField(20);
        JComboBox<String> bloodGroup = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        JTextField nearestBank = new JTextField(20);
        JTextField weight = new JTextField(20);

        styleTextField(name);
        styleTextField(mobile);
        styleTextField(address);
        styleTextField(age);
        styleTextField(nearestBank);
        styleTextField(weight);

        addField(form, gbc, 0, "Name:", name);
        addField(form, gbc, 1, "Mobile No:", mobile);
        addField(form, gbc, 2, "Address:", address);
        addField(form, gbc, 3, "Age:", age);
        addField(form, gbc, 4, "Blood Group:", bloodGroup);
        addField(form, gbc, 5, "Nearest Blood Bank:", nearestBank);
        addField(form, gbc, 6, "Weight:", weight);

        JButton submit = createPrimaryButton("Submit");
        JButton back = createSecondaryButton("Back");

        gbc.gridx = 0;
        gbc.gridy = 7;
        form.add(submit, gbc);

        gbc.gridx = 1;
        form.add(back, gbc);

        submit.addActionListener(e -> {
            String n = name.getText().trim();
            String mob = mobile.getText().trim();
            String ageStr = age.getText().trim();

            if (n.isEmpty() || mob.isEmpty() || ageStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill the required fields.");
                return;
            }

            int ageVal;
            double weightVal;
            try {
                ageVal = Integer.parseInt(ageStr);
                String weightStr = weight.getText().trim();
                weightVal = weightStr.isEmpty() ? 0 : Double.parseDouble(weightStr);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Age and Weight must be numbers.");
                return;
            }

            String sql = "INSERT INTO donors (user_id, name, mobile, address, age, blood_group, nearest_bank, weight) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                if (loggedUserId == -1) {
                    ps.setNull(1, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(1, loggedUserId);
                }
                ps.setString(2, n);
                ps.setString(3, mob);
                ps.setString(4, address.getText().trim());
                ps.setInt(5, ageVal);
                ps.setString(6, (String) bloodGroup.getSelectedItem());
                ps.setString(7, nearestBank.getText().trim());
                ps.setDouble(8, weightVal);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Donor details submitted successfully!");
                name.setText("");
                mobile.setText("");
                address.setText("");
                age.setText("");
                nearestBank.setText("");
                weight.setText("");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        back.addActionListener(e -> cardLayout.show(mainPanel, "home"));
        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // RECEIVER PAGE
    // =========================================================

    private JPanel receiverPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);
        panel.add(header("Blood Receiver"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(PANEL_BG);
        form.setBorder(new EmptyBorder(30, 100, 30, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField name = new JTextField(20);
        JTextField mobile = new JTextField(20);
        JTextField age = new JTextField(20);
        JTextField hospital = new JTextField(20);
        JTextField bystander = new JTextField(20);
        JComboBox<String> bloodGroup = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});

        styleTextField(name);
        styleTextField(mobile);
        styleTextField(age);
        styleTextField(hospital);
        styleTextField(bystander);

        addField(form, gbc, 0, "Name:", name);
        addField(form, gbc, 1, "Mobile No:", mobile);
        addField(form, gbc, 2, "Age:", age);
        addField(form, gbc, 3, "Hospital:", hospital);
        addField(form, gbc, 4, "Bystander No:", bystander);
        addField(form, gbc, 5, "Blood Group Needed:", bloodGroup);

        JButton search = createPrimaryButton("Request Blood");
        JButton back = createSecondaryButton("Back");

        gbc.gridx = 0;
        gbc.gridy = 6;
        form.add(search, gbc);

        gbc.gridx = 1;
        form.add(back, gbc);

        search.addActionListener(e -> {
            String n = name.getText().trim();
            String mob = mobile.getText().trim();
            String hosp = hospital.getText().trim();

            if (n.isEmpty() || mob.isEmpty() || hosp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.");
                return;
            }

            int ageVal = 0;
            String ageStr = age.getText().trim();
            if (!ageStr.isEmpty()) {
                try {
                    ageVal = Integer.parseInt(ageStr);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "Age must be a number.");
                    return;
                }
            }

            String group = (String) bloodGroup.getSelectedItem();
            String insertSql = "INSERT INTO receivers (user_id, name, mobile, age, hospital, bystander_no, blood_group) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            String countSql = "SELECT COUNT(*) AS total FROM donors WHERE blood_group = ?";

            try (Connection conn = DatabaseConnection.getConnection()) {

                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    if (loggedUserId == -1) {
                        ps.setNull(1, java.sql.Types.INTEGER);
                    } else {
                        ps.setInt(1, loggedUserId);
                    }
                    ps.setString(2, n);
                    ps.setString(3, mob);
                    ps.setInt(4, ageVal);
                    ps.setString(5, hosp);
                    ps.setString(6, bystander.getText().trim());
                    ps.setString(7, group);
                    ps.executeUpdate();
                }

                int matches = 0;
                try (PreparedStatement ps = conn.prepareStatement(countSql)) {
                    ps.setString(1, group);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            matches = rs.getInt("total");
                        }
                    }
                }

                JOptionPane.showMessageDialog(this,
                        "Blood request submitted successfully!\n"
                                + "Registered donors with blood group " + group + ": " + matches);

                name.setText("");
                mobile.setText("");
                age.setText("");
                hospital.setText("");
                bystander.setText("");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        back.addActionListener(e -> cardLayout.show(mainPanel, "home"));
        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // HOSPITAL PAGE
    // =========================================================

    private JPanel hospitalPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);
        panel.add(header("Hospital / Blood Bank Search"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(PANEL_BG);
        form.setBorder(new EmptyBorder(30, 100, 30, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField district = new JTextField(20);
        JTextField bloodBank = new JTextField(20);
        JTextField available = new JTextField(20);

        styleTextField(district);
        styleTextField(bloodBank);
        styleTextField(available);

        bloodBank.setEditable(false);
        available.setEditable(false);

        addField(form, gbc, 0, "District:", district);
        addField(form, gbc, 1, "Blood Bank in District:", bloodBank);
        addField(form, gbc, 2, "Available Donors:", available);

        JButton search = createPrimaryButton("Search");
        JButton back = createSecondaryButton("Back");

        gbc.gridx = 0;
        gbc.gridy = 3;
        form.add(search, gbc);

        gbc.gridx = 1;
        form.add(back, gbc);

        search.addActionListener(e -> {
            String districtName = district.getText().trim();
            if (districtName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter district name.");
                return;
            }

            String bankSql = "SELECT bank_name FROM blood_banks WHERE district LIKE ? LIMIT 1";
            String countSql = "SELECT COUNT(*) AS total FROM donors WHERE nearest_bank = ?";

            try (Connection conn = DatabaseConnection.getConnection()) {

                String bankName = null;
                try (PreparedStatement ps = conn.prepareStatement(bankSql)) {
                    ps.setString(1, "%" + districtName + "%");
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            bankName = rs.getString("bank_name");
                        }
                    }
                }

                if (bankName == null) {
                    bloodBank.setText("");
                    available.setText("");
                    JOptionPane.showMessageDialog(this, "No blood bank found for this district.");
                    return;
                }

                bloodBank.setText(bankName);

                try (PreparedStatement ps = conn.prepareStatement(countSql)) {
                    ps.setString(1, bankName);
                    try (ResultSet rs = ps.executeQuery()) {
                        available.setText(rs.next() ? String.valueOf(rs.getInt("total")) : "0");
                    }
                }

                JOptionPane.showMessageDialog(this, "Blood bank information found.");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        back.addActionListener(e -> cardLayout.show(mainPanel, "home"));
        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // PROFILE PAGE
    // =========================================================

    private JPanel profilePage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);
        panel.add(header("My Profile"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(PANEL_BG);
        center.setBorder(new EmptyBorder(40, 50, 40, 50));

        JButton edit = createPrimaryButton("Edit Profile");
        JButton remove = createSecondaryButton("Remove Profile");
        JButton back = createSecondaryButton("Back");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        center.add(edit, gbc);

        gbc.gridy++;
        center.add(remove, gbc);

        gbc.gridy++;
        center.add(back, gbc);

        edit.addActionListener(e -> {
            if (loggedUserId == -1) {
                JOptionPane.showMessageDialog(this, "You must be logged in to edit a profile.");
                return;
            }

            JTextField newName = new JTextField(loggedUser);
            Object[] message = {"Username:", newName};

            int result = JOptionPane.showConfirmDialog(this, message, "Edit Profile", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String updated = newName.getText().trim();
                if (updated.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Username cannot be empty.");
                    return;
                }

                String sql = "UPDATE users SET username = ? WHERE id = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, updated);
                    ps.setInt(2, loggedUserId);
                    ps.executeUpdate();
                    loggedUser = updated;
                    JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
                }
            }
        });

        remove.addActionListener(e -> {
            if (loggedUserId == -1) {
                JOptionPane.showMessageDialog(this, "You must be logged in to remove a profile.");
                return;
            }

            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove your profile?",
                    "Remove Profile", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM users WHERE id = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, loggedUserId);
                    ps.executeUpdate();
                    loggedUser = "";
                    loggedUserId = -1;
                    JOptionPane.showMessageDialog(this, "Profile removed.");
                    cardLayout.show(mainPanel, "login");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
                }
            }
        });

        back.addActionListener(e -> cardLayout.show(mainPanel, "home"));
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // COMMON HEADER
    // =========================================================

    private JPanel header(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(RED);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel label = new JLabel(title);
        label.setForeground(WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // COMMON BUTTONS
    // =========================================================

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(RED);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setOpaque(true);
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(WHITE);
        button.setForeground(RED);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new CompoundBorder(
                new LineBorder(RED, 1),
                new EmptyBorder(10, 20, 10, 20)
        ));
        return button;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(RED);
        button.setBackground(WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new CompoundBorder(
                new LineBorder(RED, 2),
                new EmptyBorder(25, 20, 25, 20)
        ));
        return button;
    }

    // =========================================================
    // ADD FORM FIELD
    // =========================================================

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(SOFT_TEXT);
        fieldLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(fieldLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        if (component instanceof JComboBox) {
            component.setBackground(WHITE);
        }
        panel.add(component, gbc);
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
    }

    // =========================================================
    // PASSWORD HASHING (SHA-256)
    // =========================================================

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is always available on the JVM, but wrap just in case.
            throw new RuntimeException(e);
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            BloodBankSystem app =
                    new BloodBankSystem();

            app.setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConnection::closeConnection));
    }
}
