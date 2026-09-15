import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Account Class
abstract class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    protected double balance;
    private List<String> transactionHistory;

    public BankAccount(String accountNumber, String accountHolderName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        addTransaction("Account opened with initial deposit: ₱" + initialDeposit);
    }

    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addTransaction("Deposited: ₱" + amount + " | New Balance: ₱" + balance);
            System.out.println(" Successfully deposited ₱" + amount);
        } else {
            System.out.println(" Deposit amount must be positive.");
        }
    }

    public abstract void withdraw(double amount);

    protected void addTransaction(String detail) {
        transactionHistory.add(detail);
    }

    public void printTransactionHistory() {
        System.out.println("\n--- Transaction History for " + accountNumber + " ---");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (String record : transactionHistory) {
                System.out.println(record);
            }
        }
    }
}

// Savings Rate
class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount(String accountNumber, String accountHolderName, double initialDeposit, double interestRate) {
        super(accountNumber, accountHolderName, initialDeposit);
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            addTransaction("Withdrew: ₱" + amount + " | New Balance: ₱" + balance);
            System.out.println(" Successfully withdrew ₱" + amount);
        } else {
            System.out.println(" Insufficient balance for withdrawal.");
        }
    }

    public void applyInterest() {
        double interest = balance * interestRate;
        balance += interest;
        addTransaction("Interest applied: ₱" + interest + " | New Balance: ₱" + balance);
        System.out.println(" Interest of ₱" + interest + " added.");
    }
}

// Checking Account Overdraft Protection
class CheckingAccount extends BankAccount {
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, String accountHolderName, double initialDeposit, double overdraftLimit) {
        super(accountNumber, accountHolderName, initialDeposit);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && (balance + overdraftLimit) >= amount) {
            balance -= amount;
            addTransaction("Withdrew: ₱" + amount + " | New Balance: ₱" + balance);
            System.out.println(" Successfully withdrew ₱" + amount);
        } else {
            System.out.println(" Exceeds overdraft limit.");
        }
    }
}

// Main
public class BankingSystem {
    private List<BankAccount> accounts = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        BankingSystem bank = new BankingSystem();
        bank.runMenu();
    }

    public void runMenu() {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("         DUDE's Banking          ");
            System.out.println("=================================");
            System.out.println("1. Create Savings Account");
            System.out.println("2. Create Checking Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Balance Inquiries");
            System.out.println("6. Transaction History");
            System.out.println("7. Apply Interest (Savings)");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createAccount("SAVINGS");
                case 2 -> createAccount("CHECKING");
                case 3 -> handleDeposit();
                case 4 -> handleWithdraw();
                case 5 -> handleCheckBalance();
                case 6 -> handleHistory();
                case 7 -> handleApplyInterest();
                case 8 -> {
                    System.out.println("Thank you for using Java Bank!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createAccount(String type) {
        System.out.print("Enter Account Number: ");
        String accNum = scanner.nextLine();
        System.out.print("Enter Account Holder Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Initial Deposit: ");
        double deposit = scanner.nextDouble();

        if (type.equals("SAVINGS")) {
            System.out.print("Enter Annual Interest Rate (e.g., 0.05 for 5%): ");
            double rate = scanner.nextDouble();
            accounts.add(new SavingsAccount(accNum, name, deposit, rate));
            System.out.println(" Savings Account created successfully!");
        } else {
            System.out.print("Enter Overdraft Limit: ");
            double limit = scanner.nextDouble();
            accounts.add(new CheckingAccount(accNum, name, deposit, limit));
            System.out.println(" Checking Account created successfully!");
        }
    }

    private BankAccount findAccount(String accNum) {
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equalsIgnoreCase(accNum)) {
                return acc;
            }
        }
        System.out.println(" Account not found.");
        return null;
    }

    private void handleDeposit() {
        System.out.print("Enter Account Number: ");
        BankAccount acc = findAccount(scanner.nextLine());
        if (acc != null) {
            System.out.print("Enter Amount to Deposit: ");
            acc.deposit(scanner.nextDouble());
        }
    }

    private void handleWithdraw() {
        System.out.print("Enter Account Number: ");
        BankAccount acc = findAccount(scanner.nextLine());
        if (acc != null) {
            System.out.print("Enter Amount to Withdraw: ");
            acc.withdraw(scanner.nextDouble());
        }
    }

    private void handleCheckBalance() {
        System.out.print("Enter Account Number: ");
        BankAccount acc = findAccount(scanner.nextLine());
        if (acc != null) {
            System.out.println("Account Holder: " + acc.getAccountHolderName());
            System.out.println("Current Balance: ₱" + acc.getBalance());
        }
    }

    private void handleHistory() {
        System.out.print("Enter Account Number: ");
        BankAccount acc = findAccount(scanner.nextLine());
        if (acc != null) {
            acc.printTransactionHistory();
        }
    }

    private void handleApplyInterest() {
        System.out.print("Enter Savings Account Number: ");
        BankAccount acc = findAccount(scanner.nextLine());
        if (acc instanceof SavingsAccount savings) {
            savings.applyInterest();
        } else if (acc != null) {
            System.out.println(" This operation is only available for Savings Accounts.");
        }
    }
}