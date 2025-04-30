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
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));//opens the file to read
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
       try { BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true)); //By adding true it keeps existing data and puts new transaction at the bottom

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
       }   // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
    }

    private static void addPayment(Scanner scanner) {
       try { BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true)); //By adding true it keeps existing data and puts new transaction at the bottom

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
            System.out.println("Amount must be a positive.");
            return;
        }else{
            amount = amount *-1; //changes to negative since it's a payment
        }

           Transaction newPayment = new Transaction(date, time, description, vendor, amount); //creating a new transaction object
           transactions.add(newPayment); //adds the transaction to the list
           System.out.println("Payment added successfully!");

           String line = newPayment.toString(); //call toString method on the newPayment
           writer.write(line); //writes the transaction line to the file
           writer.newLine(); // clearing line

           writer.close(); //closes writer


       } catch (Exception e) {
           System.out.println("Invalid input.");
           e.printStackTrace();
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

        //Prints header row with colum titles and fixed-width formatting
        try {
            System.out.printf("%-12s | %-10s | %-30s | %-20s | %10s\n", "Date", "Time", "Description", "Vendor", "Amount");

            //Divider line underneath headers
            System.out.println("---------------------------------------------------------------------------------------------");

            //Loop through each transaction and print details in the same formatting
            for (Transaction transaction : transactions) {
                //Using getters to retrieve the private variables in Transaction class
                System.out.printf("%-12s | %-10s | %-30s | %-20s | %10.2f\n",
                        transaction.getDate(), transaction.getTime().format(TIME_FORMATTER), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
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
                            transaction.getDate(), transaction.getTime().format(TIME_FORMATTER), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                }

            }

        } catch (Exception e) {
            System.out.println("Error has occurred while displaying deposits.");
            e.printStackTrace();


            // This method should display a table of all deposits in the `transactions` ArrayList.
            // The table should have columns for date, time, description, vendor, and amount.

        }
    }
    private static void displayPayments() {
        try {
            //Prints header row with colum titles and fixed-width formatting
            System.out.printf("%-12s | %-10s | %-30s | %-20s | %10s\n", "Date", "Time", "Description", "Vendor", "Amount");

            System.out.println("---------------------------------------------------------------------------------------------");

            //loop through each transaction in the list
            for (Transaction transaction : transactions){
                if (transaction.getAmount() < 0){
                    //Using getters to retrieve the private varibles in Transaction class
                    System.out.printf("%-12s | %-10s | %-30s | %-20s | %10s\n",
                            transaction.getDate(), transaction.getTime().format(TIME_FORMATTER), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                    //Gets first day of current month
                  LocalDate startDate = LocalDate.now().withDayOfMonth(1);

                  //Gets today's date at the end of range
                  LocalDate endDate = LocalDate.now();

                    //Filters and prints transactions that occurred from start of month to end.
                    filterTransactionsByDate(startDate, endDate);

                    break;
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "2":
                    //Sets the date to the first day of the previous month
                    LocalDate startOfPreviousMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);

                    //Gets last day of previous month by adding one month to the first day,then subtracts one day.
                    LocalDate endOfPreviousMonth = startOfPreviousMonth.plusMonths(1).minusDays(1);

                    //Filters and prints transactions that occurred in the previous month
                    filterTransactionsByDate(startOfPreviousMonth, endOfPreviousMonth);

                    break;
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "3":
                    //Gets first day of current year
                    LocalDate startOfYear = LocalDate.now().withDayOfYear(1);

                    //Gets date at the end of range
                    LocalDate endOfYear = LocalDate.now();

                    //Filters and prints transactions that occurred from start of year to today
                    filterTransactionsByDate(startOfYear, endOfYear);

                    break;
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.

                case "4":
                    //Sets the date of to the first day of the previous year
                    LocalDate startOfPreviousYear = LocalDate.now().minusYears(1).withDayOfYear(1);

                    //Gets last day of previous year by adding one year to the first day, then subtracts one day
                    LocalDate endOfPreviousYear = startOfPreviousYear.plusYears(1).minusDays(1);

                    //Filters and prints transactions that occurred in the previous year
                    filterTransactionsByDate(startOfPreviousYear,endOfPreviousYear);

                    break;
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "5":
                    System.out.println("Enter name of vendor to filter transactions by: ");

                    //Reads vendor's name
                    String vendor = scanner.nextLine().trim();

                    //Passes vendor name to method
                    filterTransactionsByVendor(vendor);

                    break;
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

        //Checking if any results were found
        boolean results = false;
        try {
            //Loop through all transactions in list
            for (Transaction transaction : transactions) {
                LocalDate date = transaction.getDate();
                // checks if transaction date is on or after start date and on or before the end date.
                if ((date.isEqual(startDate) || date.isAfter(startDate)) && (date.isEqual(endDate) || date.isBefore(endDate))){
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

    // This method filters the transactions by date and prints a report to the console.
    // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
    // The method loops through the transactions list and checks each transaction's date against the date range.
    // Transactions that fall within the date range are printed to the console.
    // If no transactions fall within the date range, the method prints a message indicating that there are no results


    //Method to filter transactions by vendor
    private static void filterTransactionsByVendor(String vendor) {

        //Checking if any results were found
        boolean results = false;
        try {
            //Loop through all transactions in list
            for (Transaction transaction : transactions) {
                //checks if vendor name of current transaction matches the input, also ignoring cases
                if (transaction.getVendor().equalsIgnoreCase(vendor)){
                    System.out.println(transaction.toString());

                    //Changes results to true if it finds matching transaction
                    results = true;
                }
            }
            //if no transactions were found with given vendor name
            if (!results){
                System.out.println("No transactions were found for vendor:");
            }
            } catch (Exception e){
            e.printStackTrace();
        }
            // This method filters the transactions by vendor and prints a report to the console.
            // It takes one parameter: vendor, which represents the name of the vendor to filter by.
            // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
            // Transactions with a matching vendor name are printed to the console.
            // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
        }

}