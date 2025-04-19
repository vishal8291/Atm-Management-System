import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class ATMSystem {
    private JFrame frame;
    private JTextField accountField, amountField;
    private JPasswordField pinField;
    private JTextArea outputArea;
    private Connection conn;

    public ATMSystem() {
        initializeDB();
        createGUI();
    }

    private void initializeDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/atm?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "root";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to database successfully.");
        } catch (Exception e) {
            System.out.println("❌ Database Connection Failed!");
            e.printStackTrace();
        }
    }

    private void createGUI() {
        frame = new JFrame("ATM System");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(8, 2, 5, 5));

        frame.add(new JLabel("Account Number:"));
        accountField = new JTextField();
        frame.add(accountField);

        frame.add(new JLabel("PIN:"));
        pinField = new JPasswordField();
        frame.add(pinField);

        JButton loginButton = new JButton("Login");
        frame.add(loginButton);

        frame.add(new JLabel("Amount:"));
        amountField = new JTextField();
        frame.add(amountField);

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton balanceButton = new JButton("Check Balance");
        JButton changePinButton = new JButton("Change PIN");

        frame.add(depositButton);
        frame.add(withdrawButton);
        frame.add(balanceButton);
        frame.add(changePinButton);

        outputArea = new JTextArea(5, 30);
        outputArea.setEditable(false);
        frame.add(new JScrollPane(outputArea));

        loginButton.addActionListener(e -> loginUser());
        depositButton.addActionListener(e -> depositMoney());
        withdrawButton.addActionListener(e -> withdrawMoney());
        balanceButton.addActionListener(e -> checkBalance());
        changePinButton.addActionListener(e -> changePin());

        frame.setVisible(true);
    }

    private void loginUser() {
        String accountNumber = accountField.getText();
        String pin = new String(pinField.getPassword());

        try {
            String query = "SELECT * FROM users2 WHERE account_number=? AND pin=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                outputArea.setText("✅ Login Successful!");
            } else {
                outputArea.setText("❌ Invalid credentials!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void depositMoney() {
        String accountNumber = accountField.getText();
        String amountText = amountField.getText();

        if (!isValidAmount(amountText)) {
            outputArea.setText("❌ Invalid Amount! Enter only numbers.");
            return;
        }

        double amount = Double.parseDouble(amountText);

        try {
            String updateBalance = "UPDATE users2 SET balance = balance + ? WHERE account_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateBalance);
            pstmt.setDouble(1, amount);
            pstmt.setString(2, accountNumber);
            pstmt.executeUpdate();

            outputArea.setText("✅ Deposited: " + amount);
        } catch (SQLException e) {
            e.printStackTrace();
            outputArea.setText("❌ Deposit Failed!");
        }
    }

    private void withdrawMoney() {
        String accountNumber = accountField.getText();
        String amountText = amountField.getText();

        if (!isValidAmount(amountText)) {
            outputArea.setText("❌ Invalid Amount! Enter only numbers.");
            return;
        }

        double amount = Double.parseDouble(amountText);

        try {
            String checkBalance = "SELECT balance FROM users2 WHERE account_number = ?";
            PreparedStatement pstmt1 = conn.prepareStatement(checkBalance);
            pstmt1.setString(1, accountNumber);
            ResultSet rs = pstmt1.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance >= amount) {
                    String updateBalance = "UPDATE users2 SET balance = balance - ? WHERE account_number = ?";
                    PreparedStatement pstmt2 = conn.prepareStatement(updateBalance);
                    pstmt2.setDouble(1, amount);
                    pstmt2.setString(2, accountNumber);
                    pstmt2.executeUpdate();
                    outputArea.setText("✅ Withdrawn: " + amount);
                } else {
                    outputArea.setText("❌ Insufficient Balance!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            outputArea.setText("❌ Withdrawal Failed!");
        }
    }

    private void checkBalance() {
        String accountNumber = accountField.getText();

        try {
            String query = "SELECT balance FROM users2 WHERE account_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                outputArea.setText("💰 Balance: " + balance);
            } else {
                outputArea.setText("❌ Account Not Found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void changePin() {
        String accountNumber = accountField.getText();

        if (accountNumber.isEmpty()) {
            outputArea.setText("❌ Please enter account number first!");
            return;
        }

        String newPin = JOptionPane.showInputDialog(frame, "Enter New 4-digit Numeric PIN:");
        if (!isValidPin(newPin)) {
            outputArea.setText("❌ Invalid PIN! Only 4-digit numbers are allowed.");
            return;
        }
        String confirmPin = JOptionPane.showInputDialog(frame, "Confirm New PIN:");
        if (!newPin.equals(confirmPin)) {
            outputArea.setText("❌ PINs do not match!");
            return;
        }

        try {
            String updatePin = "UPDATE users2 SET pin = ? WHERE account_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(updatePin);
            pstmt.setString(1, newPin);
            pstmt.setString(2, accountNumber);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                outputArea.setText("✅ PIN changed successfully.");
            } else {
                outputArea.setText("❌ Account not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            outputArea.setText("❌ PIN change failed!");
        }
    }

    private boolean isValidAmount(String amount) {
        return amount.matches("\\d+(\\.\\d{1,2})?");
    }

    private boolean isValidPin(String pin) {
        return pin != null && pin.matches("\\d{4}");
    }

    public static void main(String[] args) {
        new ATMSystem();
    }
}
