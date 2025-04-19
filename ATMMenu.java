import javax.swing.*;
import java.awt.event.*;

public class ATMMenu extends JFrame {
    public ATMMenu() {
        setTitle("ATM Menu");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton btnWithdraw = new JButton("Withdraw");
        JButton btnDeposit = new JButton("Deposit");
        JButton btnBalance = new JButton("Balance Inquiry");

        btnWithdraw.setBounds(50, 30, 200, 30);
        btnDeposit.setBounds(50, 70, 200, 30);
        btnBalance.setBounds(50, 110, 200, 30);

        add(btnWithdraw);
        add(btnDeposit);
        add(btnBalance);

        btnWithdraw.addActionListener(e -> JOptionPane.showMessageDialog(this, "Withdraw feature coming soon!"));
        btnDeposit.addActionListener(e -> JOptionPane.showMessageDialog(this, "Deposit feature coming soon!"));
        btnBalance.addActionListener(e -> JOptionPane.showMessageDialog(this, "Balance Inquiry feature coming soon!"));

        setVisible(true);
    }
    
    public static void main(String[] args) {
        new ATMMenu();
    }
}
