package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FinancialTracker {
    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        transactions = loadTransactions(FILE_NAME); //Loads transactions from the file

        Collections.sort(transactions, Comparator.comparing(Transaction::getDate)); //sorts transactions by date


        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }
    // method to load transactions from a file and return them as a list
    public static ArrayList<Transaction> loadTransactions(String fileName) {
        String line;
        ArrayList<Transaction> transactions = new ArrayList<>(); // list to store loaded transactions

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName)); //opens the file to read
            while ((line = br.readLine()) != null) { //reads the file line by line
                String[] parts = line.split("\\|"); //splits the line by '|', delimiter

                LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER); // parse date string using custom formatter
                LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER); // parse time using custom formatter
                String description = parts[2]; // extract the transaction description
                String vendor = parts[3]; // extract vendor name
                double amount = Double.parseDouble(parts[4]); // parse amount as a double



                // creates a new transaction and adds to the transaction list
                transactions.add(new Transaction(date, time, description, vendor, amount));

            }
            br.close(); //closing reader after loop

        } catch (Exception e) {
            System.out.println("Error has occurred!"); //if any errors happen, print message
            e.printStackTrace();


        }
        return transactions; // return list of transactions loaded from file

        // This method should load transactions from a file with the given file name.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
    }


    private static void addDeposit(Scanner scanner) {
       try {

           System.out.println("Enter the date and time in the following format: (yyyy-MM-dd HH:mm:ss):");
           String dateTimeInput = scanner.nextLine().trim(); //read and trims the input

           String[] parts = dateTimeInput.split(" "); //splits the input  into date and time parts
           LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER); // parse date string using custom formatter
           LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);// parse time using custom formatter

           System.out.println("Enter the description: ");
           String description = scanner.nextLine().trim(); //reads description

           System.out.println("Enter the vendor: ");
           String vendor = scanner.nextLine().trim(); //reads vendor

           System.out.println("Enter the amount: ");
           double amount = Double.parseDouble(scanner.nextLine().trim());

           if (amount <= 0) { //number must be positive
               System.out.println("Error, amount must be positive.");
               return; //exit method
           }
           Transaction newDeposit = new Transaction(date, time, description, vendor, amount); //creating a new transaction object
           transactions.add(newDeposit); //adds the transaction to the list
           System.out.println("Deposit added successfully!");

       } catch (Exception e) {
           System.out.println("Invalid. Try again!");
       }   // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
    }

    private static void addPayment(Scanner scanner) {
       try {

        System.out.println("Please enter the date and time in the following format: (yyyy-MM-dd HH:mm:ss)");
        String dateTimeInput2 = scanner.nextLine().trim(); //read and trims the input

        String[] parts = dateTimeInput2.split(" "); //splits the input into date and time parts
        LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER); // parse date string using custom formatter
        LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);// parse time using custom formatter

        System.out.println("Enter the description:  ");
        String description = scanner.nextLine().trim(); //reads description

        System.out.println("Enter the vendor: ");
        String vendor = scanner.nextLine().trim(); //reads vendor

        System.out.println("Enter the amount: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());//reads amount

        if (amount <= 0) { //checks to see if amount is positive
            System.out.println("Amount must be a positive. Please try again.");
            return;
        }else{
            amount = amount *-1; //changes to negative since it's a payment
        }

           Transaction newPayment = new Transaction(date, time, description, vendor, amount); //creating a new transaction object
           transactions.add(newPayment); //adds the transaction to the list
           System.out.println("Payment added successfully!");


       } catch (Exception e) {
           System.out.println("Invalid. Try again!");

       }
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number then transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    //Method displays all transactions from the list in a formatted table
    private static void displayLedger() {

        //Prints header row with colum titles and fixed-wdth formatting
        System.out.printf("%-12s | %-10s | %-30s | %-20s | %10s\n", "Date", "Time", "Description", "Vendor", "Amount");

        //Divider line underneath headers
        System.out.println("---------------------------------------------------------------------------------------------");

        //Loop through each transaction and print details in the same formatting
        for (Transaction transaction : transactions) {
            System.out.printf("%-12s | %-10s | %-30s | %-20s | %10.2f\n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
        }
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }

}