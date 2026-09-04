import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Blood Bank Management System
 * A clean, single-file Swing desktop UI covering login, account creation,
 * donor registration, receiver requests, hospital/blood-bank search and
 * a simple profile screen.
 */
public class BloodBankSystem extends JFrame {

    // ---- Navigation ----------------------------------------------------
    JPanel mainPanel;
    CardLayout cardLayout;

    // ---- Palette ---------------------------------------------------------
    // One primary accent (RED) does most of the work; DARK_RED is reserved
    // for hover/pressed states, DARK for body text, LIGHT_RED for canvas.
    static final Color RED       = new Color(178, 34, 52);
    static final Color DARK_RED  = new Color(140, 24, 40);
    static final Color LIGHT_RED = new Color(250, 236, 238);
    static final Color WHITE     = Color.WHITE;
    static final Color DARK      = new Color(51, 51, 51);
    static final Color BORDER    = new Color(225, 200, 203);

    static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 26);
    static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD, 22);
    static final Font FONT_LABEL   = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font FONT_FIELD   = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 14);
    static final Font FONT_MENU    = new Font("Segoe UI", Font.BOLD, 17);

    String loggedUser = "";

    public BloodBankSystem() {

        setTitle("Blood Bank Management System");
        setSize(1000, 650);
        setMinimumSize(new Dimension(820, 560));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(35, 45, 35, 45)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("BLOOD BANK SYSTEM");
        title.setFont(FONT_TITLE);
        title.setForeground(RED);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("Sign in to continue");
        subtitle.setFont(FONT_LABEL);
        subtitle.setForeground(DARK);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField username = createTextField();
        JPasswordField password = createPasswordField();

        RoundedButton login = new RoundedButton("Login", true);
        RoundedButton create = new RoundedButton("Create Account", false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        box.add(title, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(2, 8, 22, 8);
        box.add(subtitle, gbc);
        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.gridwidth = 1;

        gbc.gridy++;
        gbc.gridx = 0;
        box.add(fieldLabel("Username"), gbc);
        gbc.gridx = 1;
        box.add(username, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        box.add(fieldLabel("Password"), gbc);
        gbc.gridx = 1;
        box.add(password, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 6, 8);
        box.add(login, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(6, 8, 8, 8);
        box.add(create, gbc);

        login.addActionListener(e -> {

            if (username.getText().trim().isEmpty() ||
                    password.getPassword().length == 0) {
                showInfo("Please enter username and password.");
                return;
            }

            loggedUser = username.getText().trim();
            showInfo("Login successful!");
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

        JPanel form = formCard();
        GridBagConstraints gbc = formConstraints();

        JTextField username = createTextField();
        JPasswordField password = createPasswordField();
        JPasswordField confirm = createPasswordField();
        JTextField mobile = createTextField();

        addField(form, gbc, 0, "Username", username);
        addField(form, gbc, 1, "Create Password", password);
        addField(form, gbc, 2, "Confirm Password", confirm);
        addField(form, gbc, 3, "Mobile No.", mobile);

        RoundedButton create = new RoundedButton("Create Account", true);
        RoundedButton back = new RoundedButton("Back to Login", false);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(24, 10, 10, 10);
        form.add(create, gbc);

        gbc.gridx = 1;
        form.add(back, gbc);

        create.addActionListener(e -> {

            if (username.getText().trim().isEmpty() ||
                    password.getPassword().length == 0 ||
                    confirm.getPassword().length == 0 ||
                    mobile.getText().trim().isEmpty()) {
                showInfo("Please fill all fields.");
                return;
            }

            if (!new String(password.getPassword())
                    .equals(new String(confirm.getPassword()))) {
                showInfo("Passwords do not match.");
                return;
            }

            showInfo("Account created successfully!");
            cardLayout.show(mainPanel, "login");
        });

        back.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        panel.add(wrapScroll(form), BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // HOME PAGE
    // =========================================================

    private JPanel homePage() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);
        panel.add(header("Blood Bank Management System"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 2, 22, 22));
        center.setBackground(LIGHT_RED);
        center.setBorder(new EmptyBorder(50, 90, 50, 90));

        MenuButton donor = new MenuButton("Donor", "Register as a blood donor");
        MenuButton receiver = new MenuButton("Receiver", "Request blood for a patient");
        MenuButton hospital = new MenuButton("Hospital", "Find a nearby blood bank");
        MenuButton profile = new MenuButton("Profile", "View and manage your account");

        center.add(donor);
        center.add(receiver);
        center.add(hospital);
        center.add(profile);

        panel.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 14));
        bottom.setBackground(LIGHT_RED);
        RoundedButton logout = new RoundedButton("Logout", false);
        bottom.add(logout);
        panel.add(bottom, BorderLayout.SOUTH);

        logout.addActionListener(e -> {
            loggedUser = "";
            cardLayout.show(mainPanel, "login");
        });

        donor.addActionListener(e -> cardLayout.show(mainPanel, "donor"));
        receiver.addActionListener(e -> cardLayout.show(mainPanel, "receiver"));
        hospital.addActionListener(e -> cardLayout.show(mainPanel, "hospital"));
        profile.addActionListener(e -> cardLayout.show(mainPanel, "profile"));

        return panel;
    }

    // =========================================================
    // DONOR PAGE
    // =========================================================

    private JPanel donorPage() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);
        panel.add(header("Donor Registration"), BorderLayout.NORTH);

        JPanel form = formCard();
        GridBagConstraints gbc = formConstraints();

        JTextField name = createTextField();
        JTextField mobile = createTextField();
        JTextField address = createTextField();
        JTextField age = createTextField();
        JComboBox<String> bloodGroup = createComboBox();
        JTextField nearestBank = createTextField();
        JTextField weight = createTextField();

        addField(form, gbc, 0, "Name", name);
        addField(form, gbc, 1, "Mobile No.", mobile);
        addField(form, gbc, 2, "Address", address);
        addField(form, gbc, 3, "Age", age);
        addField(form, gbc, 4, "Blood Group", bloodGroup);
        addField(form, gbc, 5, "Nearest Blood Bank", nearestBank);
        addField(form, gbc, 6, "Weight (kg)", weight);

        RoundedButton submit = new RoundedButton("Submit", true);
        RoundedButton back = new RoundedButton("Back", false);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.insets = new Insets(22, 10, 10, 10);
        form.add(submit, gbc);

        gbc.gridx = 1;
        form.add(back, gbc);

        submit.addActionListener(e -> {

            if (name.getText().trim().isEmpty() ||
                    mobile.getText().trim().isEmpty() ||
                    age.getText().trim().isEmpty()) {
                showInfo("Please fill the required fields.");
                return;
            }

            showInfo("Donor details submitted successfully!");
        });

        back.addActionListener(e -> cardLayout.show(mainPanel, "home"));

        panel.add(wrapScroll(form), BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // RECEIVER PAGE
    // =========================================================

    private JPanel receiverPage() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);
        panel.add(header("Blood Receiver"), BorderLayout.NORTH);

        JPanel form = formCard();
        GridBagConstraints gbc = formConstraints();

        JTextField name = createTextField();
        JTextField mobile = createTextField();
        JTextField age = createTextField();
        JTextField hospital = createTextField();
        JTextField bystander = createTextField();

        addField(form, gbc, 0, "Name", name);
        addField(form, gbc, 1, "Mobile No.", mobile);
        addField(form, gbc, 2, "Age", age);
        addField(form, gbc, 3, "Hospital", hospital);
        addField(form, gbc, 4, "Bystander No.", bystander);

        RoundedButton request = new RoundedButton("Request Blood", true);
        RoundedButton back = new RoundedButton("Back", false);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(22, 10, 10, 10);
        form.add(request, gbc);

        gbc.gridx = 1;
        form.add(back, gbc);

        request.addActionListener(e -> {

            if (name.getText().trim().isEmpty() ||
                    mobile.getText().trim().isEmpty() ||
                    hospital.getText().trim().isEmpty()) {
                showInfo("Please fill all required fields.");
                return;
            }

            showInfo("Blood request submitted successfully!");
        });

        back.addActionListener(e -> cardLayout.show(mainPanel, "home"));

        panel.add(wrapScroll(form), BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // HOSPITAL PAGE
    // =========================================================

    private JPanel hospitalPage() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);
        panel.add(header("Hospital / Blood Bank Search"), BorderLayout.NORTH);

        JPanel form = formCard();
        GridBagConstraints gbc = formConstraints();

        JTextField district = createTextField();
        JTextField bloodBank = createTextField();
        bloodBank.setEditable(false);
        JTextField available = createTextField();
        available.setEditable(false);

        addField(form, gbc, 0, "District", district);
        addField(form, gbc, 1, "Blood Bank in District", bloodBank);
        addField(form, gbc, 2, "Available Donors", available);

        RoundedButton search = new RoundedButton("Search", true);
        RoundedButton back = new RoundedButton("Back", false);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(22, 10, 10, 10);
        form.add(search, gbc);

        gbc.gridx = 1;
        form.add(back, gbc);

        search.addActionListener(e -> {

            String districtName = district.getText().trim();

            if (districtName.isEmpty()) {
                showInfo("Enter district name.");
                return;
            }

            bloodBank.setText("City Blood Bank");
            available.setText("25");
            showInfo("Blood bank information found.");
        });

        back.addActionListener(e -> cardLayout.show(mainPanel, "home"));

        panel.add(wrapScroll(form), BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // PROFILE PAGE
    // =========================================================

    private JPanel profilePage() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);
        panel.add(header("My Profile"), BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(40, 60, 40, 60)
        ));

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(LIGHT_RED);
        wrap.add(card);

        RoundedButton edit = new RoundedButton("Edit Profile", true);
        RoundedButton remove = new RoundedButton("Remove Profile", false);
        RoundedButton back = new RoundedButton("Back", false);

        edit.setPreferredSize(new Dimension(220, 42));
        remove.setPreferredSize(new Dimension(220, 42));
        back.setPreferredSize(new Dimension(220, 42));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;

        gbc.gridy = 0;
        card.add(edit, gbc);
        gbc.gridy = 1;
        card.add(remove, gbc);
        gbc.gridy = 2;
        card.add(back, gbc);

        edit.addActionListener(e -> {

            JTextField newName = createTextField();
            newName.setText(loggedUser);

            Object[] message = {"Username:", newName};

            int result = JOptionPane.showConfirmDialog(
                    this, message, "Edit Profile", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                loggedUser = newName.getText();
                showInfo("Profile updated successfully!");
            }
        });

        remove.addActionListener(e -> {

            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove your profile?",
                    "Remove Profile",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                loggedUser = "";
                showInfo("Profile removed.");
                cardLayout.show(mainPanel, "login");
            }
        });

        back.addActionListener(e -> cardLayout.show(mainPanel, "home"));

        panel.add(wrap, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // SHARED UI HELPERS
    // =========================================================

    /** Page header banner used on every non-login screen. */
    private JPanel header(String title) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(RED);
        panel.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel label = new JLabel(title);
        label.setForeground(WHITE);
        label.setFont(FONT_HEADER);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    /** Standard white card used to hold a GridBagLayout form. */
    private JPanel formCard() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(30, 90, 30, 90));
        return form;
    }

    private GridBagConstraints formConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 10, 9, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        return gbc;
    }

    /** Wraps a form in a scroll pane so smaller windows never clip fields. */
    private JScrollPane wrapScroll(JPanel form) {
        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(DARK);
        return label;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row,
                           String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(fieldLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(component, gbc);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(18);
        styleField(field);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(18);
        styleField(field);
        return field;
    }

    private void styleField(JTextField field) {
        field.setFont(FONT_FIELD);
        field.setForeground(DARK);
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(7, 10, 7, 10)
        ));
    }

    private JComboBox<String> createComboBox() {
        JComboBox<String> box = new JComboBox<>(new String[]{
                "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        });
        box.setFont(FONT_FIELD);
        box.setBackground(WHITE);
        box.setBorder(new LineBorder(BORDER, 1, true));
        return box;
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Blood Bank System",
                JOptionPane.PLAIN_MESSAGE);
    }

    // =========================================================
    // ROUNDED BUTTON (primary / outline)
    // =========================================================

    /** Flat, rounded button with a hover/press state. Two styles: filled (primary) or outline. */
    static class RoundedButton extends JButton {

        private final boolean filled;
        private boolean hover = false;

        RoundedButton(String text, boolean filled) {
            super(text);
            this.filled = filled;

            setFont(FONT_BUTTON);
            setForeground(filled ? WHITE : RED);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(11, 26, 11, 26));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = 12;
            RoundRectangle2D shape = new RoundRectangle2D.Float(
                    0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

            if (filled) {
                g2.setColor(hover ? DARK_RED : RED);
                g2.fill(shape);
            } else {
                g2.setColor(hover ? LIGHT_RED : WHITE);
                g2.fill(shape);
                g2.setColor(RED);
                g2.setStroke(new BasicStroke(1.4f));
                g2.draw(shape);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =========================================================
    // MENU BUTTON (home screen tiles)
    // =========================================================

    /** Large tile-style button used on the home screen, with a title and short subtitle. */
    static class MenuButton extends JPanel {

        private boolean hover = false;
        private final java.util.List<ActionListener> listeners = new java.util.ArrayList<>();

        MenuButton(String title, String subtitle) {
            setLayout(new GridBagLayout());
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel titleLabel = new JLabel(title.toUpperCase());
            titleLabel.setFont(FONT_MENU);
            titleLabel.setForeground(RED);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel subLabel = new JLabel(subtitle);
            subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            subLabel.setForeground(DARK);
            subLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel textPanel = new JPanel();
            textPanel.setOpaque(false);
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            textPanel.add(titleLabel);
            textPanel.add(Box.createVerticalStrut(6));
            textPanel.add(subLabel);

            add(textPanel);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    ActionEvent evt = new ActionEvent(MenuButton.this,
                            ActionEvent.ACTION_PERFORMED, "click");
                    for (ActionListener l : listeners) l.actionPerformed(evt);
                }
            });
        }

        void addActionListener(ActionListener l) {
            listeners.add(l);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = 16;
            RoundRectangle2D shape = new RoundRectangle2D.Float(
                    0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

            g2.setColor(hover ? LIGHT_RED : WHITE);
            g2.fill(shape);
            g2.setColor(RED);
            g2.setStroke(new BasicStroke(1.6f));
            g2.draw(shape);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // fall back to the default cross-platform look
        }

        SwingUtilities.invokeLater(() -> {
            BloodBankSystem app = new BloodBankSystem();
            app.setVisible(true);
        });
    }
}
