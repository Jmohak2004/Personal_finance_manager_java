import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class PersonalFinanceManager extends JFrame {
    private JTextField txtAmount, txtDescription, txtSavingsGoal;
    private JComboBox<String> comboCategory;
    private DefaultTableModel tableModel;
    private JTable transactionTable;
    private JLabel lblTotalIncome, lblTotalExpense, lblSavingsGoal, lblSavingsProgress;
    private double savingsGoal = 0.0;

    public PersonalFinanceManager() {
        setTitle("Personal Finance Manager");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));

        inputPanel.add(new JLabel("Amount:"));
        txtAmount = new JTextField();
        inputPanel.add(txtAmount);

        inputPanel.add(new JLabel("Description:"));
        txtDescription = new JTextField();
        inputPanel.add(txtDescription);

        inputPanel.add(new JLabel("Category:"));
        comboCategory = new JComboBox<>(new String[]{"Income", "Food", "Rent", "Utilities", "Entertainment"});
        inputPanel.add(comboCategory);

        JButton btnAddTransaction = new JButton("Add Transaction");
        inputPanel.add(btnAddTransaction);

        inputPanel.add(new JLabel("Savings Goal:"));
        txtSavingsGoal = new JTextField();
        inputPanel.add(txtSavingsGoal);

        JButton btnSetSavingsGoal = new JButton("Set Savings Goal");
        inputPanel.add(btnSetSavingsGoal);

        add(inputPanel, BorderLayout.NORTH);

        String[] columns = {"Amount", "Description", "Category"};
        tableModel = new DefaultTableModel(columns, 0);
        transactionTable = new JTable(tableModel);
        add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(2, 2));
        lblTotalIncome = new JLabel("Total Income: $0.00");
        lblTotalExpense = new JLabel("Total Expenses: $0.00");
        lblSavingsGoal = new JLabel("Savings Goal: $0.00");
        lblSavingsProgress = new JLabel("Savings Progress: $0.00");
        summaryPanel.add(lblTotalIncome);
        summaryPanel.add(lblTotalExpense);
        summaryPanel.add(lblSavingsGoal);
        summaryPanel.add(lblSavingsProgress);
        add(summaryPanel, BorderLayout.SOUTH);

        btnAddTransaction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTransaction();
            }
        });

        btnSetSavingsGoal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSavingsGoal();
            }
        });
    }

    private void addTransaction() {
        String amountStr = txtAmount.getText();
        String description = txtDescription.getText();
        String category = (String) comboCategory.getSelectedItem();

        if (amountStr.isEmpty() || description.isEmpty() || category == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.addRow(new Object[]{amount, description, category});
        updateTotals(amount, category);

        txtAmount.setText("");
        txtDescription.setText("");
        comboCategory.setSelectedIndex(0);
    }

    private void updateTotals(double amount, String category) {
        double totalIncome = Double.parseDouble(lblTotalIncome.getText().replace("Total Income: $", ""));
        double totalExpense = Double.parseDouble(lblTotalExpense.getText().replace("Total Expenses: $", ""));

        if (category.equals("Income")) {
            totalIncome += amount;
            lblTotalIncome.setText("Total Income: $" + String.format("%.2f", totalIncome));
        } else {
            totalExpense += amount;
            lblTotalExpense.setText("Total Expenses: $" + String.format("%.2f", totalExpense));
        }

        if (totalIncome > 0 && totalExpense > totalIncome * 0.5) {
            JOptionPane.showMessageDialog(this, "Warning: Your expenses have exceeded 50% of your income!", "Expense Alert", JOptionPane.WARNING_MESSAGE);
        }

        if (savingsGoal > 0) {
            double savingsProgress = totalIncome - totalExpense;
            lblSavingsProgress.setText("Savings Progress: $" + String.format("%.2f", savingsProgress));
        }
    }

    private void setSavingsGoal() {
        String savingsGoalStr = txtSavingsGoal.getText();
        try {
            savingsGoal = Double.parseDouble(savingsGoalStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid savings goal amount", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        lblSavingsGoal.setText("Savings Goal: $" + String.format("%.2f", savingsGoal));
    }

    public static void main(String[] args) {
        String username = JOptionPane.showInputDialog(null, "Enter Username:");
        String password = JOptionPane.showInputDialog(null, "Enter Password:");

        if (username.equals("admin") && password.equals("password")) {
            SwingUtilities.invokeLater(() -> {
                PersonalFinanceManager manager = new PersonalFinanceManager();
                manager.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(null, "Invalid credentials!", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
