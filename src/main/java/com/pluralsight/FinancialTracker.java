package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FinancialTracker {
    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT); //changed to public so i can access it in my Transaction class for toString
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);//changed to public so i can access it in my Transaction class for toString

    public static void main(String[] args) {
        loadTransactions(FILE_NAME); //Loads transactions from the file

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
                    System.out.println("Exiting from application!");
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
    public static void loadTransactions(String fileName) {
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));//opens the file to read
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

    }
    //Method to add deposits into file
    private static void addDeposit(Scanner scanner) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true)); //By adding true it keeps existing data and puts new transaction at the bottom

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
            double amount = Double.parseDouble(scanner.nextLine().trim()); //reading line of input as a string

            if (amount <= 0) { //number must be positive
                System.out.println("Error, amount must be positive.");
                return; //exit method
            }
            Transaction newDeposit = new Transaction(date, time, description, vendor, amount); //creating a new transaction object

            transactions.add(newDeposit); //adds the transaction to the list

            System.out.println("Deposit added successfully!");

            String line = newDeposit.toString(); //call toString method on the newDeposit
            writer.write(line); //writes the transaction line to the file
            writer.newLine(); // clearing line

            writer.close(); //closes writer

        } catch (Exception e) {
            System.out.println("Invalid. Try again!");
        }
    }

    //Method to add payments to file
    private static void addPayment(Scanner scanner) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true)); //By adding true it keeps existing data and puts new transaction at the bottom

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
            double amount = Double.parseDouble(scanner.nextLine().trim());//reads amount and parses it into a double

            if (amount <= 0) { //checks to see if amount is positive
                System.out.println("Amount must be a positive.");
                return;
            } else {
                amount = amount * -1; //changes to negative since it's a payment
            }

            Transaction newPayment = new Transaction(date, time, description, vendor, amount); //creating a new transaction object
            transactions.add(newPayment); //adds the transaction to the list
            System.out.println("Payment added successfully!");

            String line = newPayment.toString(); //call toString method on the newPayment
            writer.write(line); //writes the string of the current transaction
            writer.newLine(); // clearing line

            writer.close(); //closes writer


        } catch (Exception e) {
            System.out.println("Invalid input.");
            e.printStackTrace();
        }
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
        //Prints header row with colum titles and fixed-width formatting
        try {
            System.out.printf("%-12s | %-10s | %-30s | %-20s | %10s\n", "Date", "Time", "Description", "Vendor", "Amount");

            //Divider line underneath headers
            System.out.println("---------------------------------------------------------------------------------------------");

            //Loop through each transaction and print details in the same formatting
            for (Transaction transaction : transactions) {
                //Using getters to retrieve the private variables in Transaction class
                System.out.printf("%-12s | %-10s | %-30s | %-20s | %10.2f\n",
                        transaction.getDate().format(DATE_FORMATTER), transaction.getTime().format(TIME_FORMATTER), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayDeposits() {
        try {
            //Prints header row with colum titles and fixed-width formatting
            System.out.printf("%-12s | %-10s | %-30s | %-20s | %10s\n", "Date", "Time", "Description", "Vendor", "Amount");

            System.out.println("---------------------------------------------------------------------------------------------");

            //loop through each transaction in the list
            for (Transaction transaction : transactions) {
                if (transaction.getAmount() > 0) {
                    //Using getters to retrieve the private variables in Transaction class
                    System.out.printf("%-12s | %-10s | %-30s | %-20s | %10s\n",
                            transaction.getDate().format(DATE_FORMATTER), transaction.getTime().format(TIME_FORMATTER), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                }

            }

        } catch (Exception e) {
            System.out.println("Error has occurred while displaying deposits.");
            e.printStackTrace();
        }
    }

    private static void displayPayments() {
        try {
            //Prints header row with colum titles and fixed-width formatting
            System.out.printf("%-12s | %-10s | %-30s | %-20s | %10s\n", "Date", "Time", "Description", "Vendor", "Amount");

            System.out.println("---------------------------------------------------------------------------------------------");

            //loop through each transaction in the list
            for (Transaction transaction : transactions) {
                if (transaction.getAmount() < 0) {
                    //Using getters to retrieve the private varibles in Transaction class
                    System.out.printf("%-12s | %-10s | %-30s | %-20s | %10s\n",
                            transaction.getDate().format(DATE_FORMATTER), transaction.getTime().format(TIME_FORMATTER), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    //Gets first day of current month
                    LocalDate startDate = LocalDate.now().withDayOfMonth(1);

                    //Gets today's date at the end of range
                    LocalDate endDate = LocalDate.now();

                    //Filters and prints transactions that occurred from start of month to end.
                    filterTransactionsByDate(startDate, endDate);

                    break;
                case "2":
                    //Sets the date to the first day of the previous month
                    LocalDate startOfPreviousMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);

                    //Gets last day of previous month by adding one month to the first day,then subtracts one day.
                    LocalDate endOfPreviousMonth = startOfPreviousMonth.plusMonths(1).minusDays(1);

                    //Filters and prints transactions that occurred in the previous month
                    filterTransactionsByDate(startOfPreviousMonth, endOfPreviousMonth);

                    break;
                case "3":
                    //Gets first day of current year
                    LocalDate startOfYear = LocalDate.now().withDayOfYear(1);

                    //Gets todays date as end of range
                    LocalDate endOfYear = LocalDate.now();

                    //Filters and prints transactions that occurred from start of year to today
                    filterTransactionsByDate(startOfYear, endOfYear);

                    break;
                case "4":
                    //Sets the date of to the first day of the previous year
                    LocalDate startOfPreviousYear = LocalDate.now().minusYears(1).withDayOfYear(1);

                    //Gets last day of previous year by adding one year to the first day, then subtracts one day
                    LocalDate endOfPreviousYear = startOfPreviousYear.plusYears(1).minusDays(1);

                    //Filters and prints transactions that occurred in the previous year
                    filterTransactionsByDate(startOfPreviousYear, endOfPreviousYear);

                    break;
                case "5":
                    System.out.println("Enter name of vendor to filter transactions by: ");

                    //Reads vendor's name
                    String vendor = scanner.nextLine().trim();

                    //Passes vendor name to method
                    filterTransactionsByVendor(vendor);

                    break;
                case "6":
                    //this will be for custom search
                    customSearchMenu(scanner);
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    //Method to filter transactions by date
    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {

        //Checking if any results were found
        boolean results = false;
        try {
            //Loop through all transactions in list
            for (Transaction transaction : transactions) {
                LocalDate date = transaction.getDate();
                // checks if transaction date is on or after start date and on or before the end date.
                if ((date.isEqual(startDate) || date.isAfter(startDate)) && (date.isEqual(endDate) || date.isBefore(endDate))) {
                    System.out.println(transaction.toString());
                    results = true; // Found matching result
                }
            }
            //if no transactions were found, print error
            if (!results) {
                System.out.println("No transactions were found for given date range.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to filter transactions by vendor
    private static void filterTransactionsByVendor(String vendor) {

        //Checking if any results were found
        boolean results = false;
        try {
            //Loop through all transactions in list
            for (Transaction transaction : transactions) {
                //checks if vendor name of current transaction matches the input, also ignoring cases
                if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                    System.out.println(transaction);

                    //Changes results to true if it finds matching transaction
                    results = true;
                }
            }
            //if no transactions were found with given vendor name
            if (!results) {
                System.out.println("No transactions were found for vendor: " + vendor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        private static void customSearchMenu(Scanner scanner){
            boolean running = true;

            while (running) {

                System.out.println("Custom Search");
                System.out.println("Choose an option:");
                System.out.println("1) Search by Date Range");
                System.out.println("2) Search by Vendor");
                System.out.println("3) Search by Description");
                System.out.println("4) Search by Amount");
                System.out.println("0) Back");

                String input = scanner.nextLine().trim();

                switch (input) {
                    case "1":
                        //Date range for custom search
                        System.out.println("Enter the Start Date (yyyy-MM-dd): ");

                        LocalDate startDate = LocalDate.parse(scanner.nextLine().trim());

                        System.out.println("Enter the End Date (yyyy-MM-dd): ");

                        LocalDate endDate = LocalDate.parse(scanner.nextLine().trim());
                        filterTransactionsByDate(startDate, endDate);

                        break;

                    case "2":
                        System.out.println("Enter the Vendor Name:");

                        String vendor = scanner.nextLine().trim();//need to fix

                        filterTransactionsByVendor(vendor);

                        break;

                    case "3":
                        //description
                        String description = scanner.nextLine.trim();
                        filterTransactionsByDescription

                        break;

                    case "4":
                        break;

                    case "0":
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid option");
                        break;
                }
            }
        }

        private static void filterTransactionsByDescription(String description) {

            //Checking if any results were found
            boolean results = false;
            try {
                //Loop through all transactions in list
                for (Transaction transaction : transactions) {
                    if (transaction.getDescription().equalsIgnoreCase(description)) {
                        System.out.println(transaction);

                        results = true;
                    }

                    }
                if (!results){
                    System.out.println("No transactions were found with the description: " + description);
                }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }

    }