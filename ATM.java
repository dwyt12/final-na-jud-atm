package AtmProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ATM {
    private double savingsBalance = 0.0;
    private double currentBalance = 0.0;
    private ArrayList<String> savingsHistory = new ArrayList<>();
    private ArrayList<String> currentHistory = new ArrayList<>();
    private String pin = "123456"; 

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String selectedAccount = "";
    private String userName = "User"; 

    private final Color BACKGROUND_COLOR = new Color(240, 248, 255); 
    private final Color BUTTON_COLOR = new Color(70, 130, 180); 
    private final Color TEXT_COLOR = new Color(0, 0, 0); 
    private final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 16);
    private final Font HEADING_FONT = new Font("Arial", Font.BOLD, 24);

    public static void main(String[] args) {
        new ATM().createGUI();
    }

    private void createGUI() {
        frame = new JFrame("ATM System");
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel greetingPanel = createGreetingPanel();
        JPanel loginPanel = createLoginPanel();
        JPanel accountSelectPanel = createAccountSelectPanel();
        JPanel menuPanel = createMenuPanel();

        mainPanel.add(greetingPanel, "Greeting");
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(accountSelectPanel, "AccountSelect");
        mainPanel.add(menuPanel, "Menu");

        frame.add(mainPanel);
        
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
     
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
       
        frame.setVisible(true);

      
        cardLayout.show(mainPanel, "Greeting");
  
      
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        cardLayout.show(mainPanel, "Greeting"); 
    }

    private JPanel createGreetingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JLabel greetingLabel = new JLabel("Welcome to the ATM, " + userName + "!", SwingConstants.CENTER);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 20));
        greetingLabel.setForeground(new Color(70, 130, 180));

        JButton proceedButton = createCoolButton("Proceed to Login");
        proceedButton.addActionListener(e -> cardLayout.show(mainPanel, "Login")); 

        panel.add(greetingLabel, BorderLayout.CENTER);
        panel.add(proceedButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

               JLabel pinLabel = createCoolLabel("Enter PIN:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(pinLabel, gbc);

        
        JPasswordField pinField = new JPasswordField(6);
        pinField.setFont(new Font("Arial", Font.PLAIN, 18));
        pinField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        
      
        pinField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
               
                if (pinField.getText().length() >= 6 || !Character.isDigit(e.getKeyChar())) {
                    e.consume(); 
                }
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(pinField, gbc);

       
        JLabel errorLabel = new JLabel("Invalid PIN", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false); 
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(errorLabel, gbc);

       
        JButton enterButton = createCoolButton("Enter");
        enterButton.addActionListener(e -> {
            String enteredPin = new String(pinField.getPassword());
            if (enteredPin.equals(pin)) {
                errorLabel.setVisible(false);
                cardLayout.show(mainPanel, "AccountSelect");
            } else {
                errorLabel.setVisible(true); 
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(enterButton, gbc);

        return panel;
    }

    private JPanel createAccountSelectPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel selectLabel = createCoolLabel("Select Account Type");
        JButton savingsButton = createCoolButton("Savings");
        JButton currentButton = createCoolButton("Current");

        savingsButton.addActionListener(e -> {
            selectedAccount = "Savings";
            cardLayout.show(mainPanel, "Menu");
        });

        currentButton.addActionListener(e -> {
            selectedAccount = "Current";
            cardLayout.show(mainPanel, "Menu");
        });

        panel.add(selectLabel);
        panel.add(savingsButton);
        panel.add(currentButton);

        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel menuLabel = createCoolLabel("ATM Menu");
        JButton depositButton = createCoolButton("Deposit");
        JButton withdrawButton = createCoolButton("Withdraw");
        JButton balanceInquiryButton = createCoolButton("Balance Inquiry");
        JButton exitButton = createCoolButton("Exit");

        depositButton.addActionListener(this::deposit);
        withdrawButton.addActionListener(this::withdraw);
        balanceInquiryButton.addActionListener(this::balanceInquiry);
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(menuLabel);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(balanceInquiryButton);
        panel.add(exitButton);

        return panel;
    }

    private void deposit(ActionEvent e) {
        String amount = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
        try {
            double depositAmount = Double.parseDouble(amount);
            if (depositAmount > 0) {
                if (selectedAccount.equals("Savings")) {
                    savingsBalance += depositAmount;
                    recordTransaction("Deposited", depositAmount, savingsHistory, savingsBalance);
                } else {
                    currentBalance += depositAmount;
                    recordTransaction("Deposited", depositAmount, currentHistory, currentBalance);
                }

                printReceipt("Deposit", depositAmount);

                int response = JOptionPane.showConfirmDialog(frame, "Do you wish to continue?", "Continue", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.NO_OPTION) System.exit(0);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid amount.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a number.");
        }
    }

    private void withdraw(ActionEvent e) {
        String amount = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
        try {
            double withdrawAmount = Double.parseDouble(amount);
            if (withdrawAmount > 0) {
                if (selectedAccount.equals("Savings") && withdrawAmount <= savingsBalance) {
                    savingsBalance -= withdrawAmount;
                    recordTransaction("Withdrew", withdrawAmount, savingsHistory, savingsBalance);
                    printReceipt("Withdrawal", withdrawAmount);

                } else if (selectedAccount.equals("Current") && withdrawAmount <= currentBalance) {
                    currentBalance -= withdrawAmount;
                    recordTransaction("Withdrew", withdrawAmount, currentHistory, currentBalance);
                    printReceipt("Withdrawal", withdrawAmount);

                } else {
                    JOptionPane.showMessageDialog(frame, "Insufficient funds.");
                }

                int response = JOptionPane.showConfirmDialog(frame, "Do you wish to continue?", "Continue", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.NO_OPTION) System.exit(0);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid amount.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a number.");
        }
    }

    private void balanceInquiry(ActionEvent e) {
        double balance = selectedAccount.equals("Savings") ? savingsBalance : currentBalance;
        JOptionPane.showMessageDialog(frame, selectedAccount + " Account Balance: " + balance + " PHP", "Balance Inquiry", JOptionPane.INFORMATION_MESSAGE);
    }

    private void recordTransaction(String action, double amount, ArrayList<String> history, double balance) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        history.add(action + ": " + amount + " PHP on " + date);
    }

    private void printReceipt(String action, double amount) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);
        double balance = selectedAccount.equals("Savings") ? savingsBalance : currentBalance;

        String receipt = """
                -------------------------
                       ATM RECEIPT       
                -------------------------
            
                Date: %s
                Account: %s
                Transaction: %s
                Amount: %.2f PHP
                Current Balance: %.2f PHP
                -------------------------
                Thank you for banking with us!
                """.formatted(date, selectedAccount, action, amount, balance);

        JOptionPane.showMessageDialog(frame, receipt, "Transaction Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    private JLabel createCoolLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(HEADING_FONT);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JButton createCoolButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        return button;
    }
}




