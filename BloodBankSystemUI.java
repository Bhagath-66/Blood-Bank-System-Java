import java.awt.*;
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
            if (username.getText().trim().isEmpty() || password.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.");
                return;
            }

            loggedUser = username.getText();
            JOptionPane.showMessageDialog(this, "Login successful!");
            cardLayout.show(mainPanel, "home");
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
            if (username.getText().trim().isEmpty() ||
                    password.getPassword().length == 0 ||
                    confirm.getPassword().length == 0 ||
                    mobile.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            if (!new String(password.getPassword()).equals(new String(confirm.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Account created successfully!");
            cardLayout.show(mainPanel, "login");
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
            if (name.getText().trim().isEmpty() || mobile.getText().trim().isEmpty() || age.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill the required fields.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Donor details submitted successfully!");
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

        JButton search = createPrimaryButton("Request Blood");
        JButton back = createSecondaryButton("Back");

        gbc.gridx = 0;
        gbc.gridy = 5;
        form.add(search, gbc);

        gbc.gridx = 1;
        form.add(back, gbc);

        search.addActionListener(e -> {
            if (name.getText().trim().isEmpty() || mobile.getText().trim().isEmpty() || hospital.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Blood request submitted successfully!");
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

            bloodBank.setText("City Blood Bank");
            available.setText("25");
            JOptionPane.showMessageDialog(this, "Blood bank information found.");
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
            JTextField newName = new JTextField(loggedUser);
            Object[] message = {"Username:", newName};

            int result = JOptionPane.showConfirmDialog(this, message, "Edit Profile", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                loggedUser = newName.getText();
                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            }
        });

        remove.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove your profile?", "Remove Profile", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                loggedUser = "";
                JOptionPane.showMessageDialog(this, "Profile removed.");
                cardLayout.show(mainPanel, "login");
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
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            BloodBankSystem app =
                    new BloodBankSystem();

            app.setVisible(true);
        });
    }
}