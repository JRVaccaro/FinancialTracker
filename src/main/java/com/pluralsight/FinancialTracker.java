package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//Main class
public class FinancialTracker {
    //list to store all transaction records
    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    //constant for file name where transaction data is stored
    private static final String FILE_NAME = "transactions.csv";

    //Constant for date format
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    //constant for time format
    private static final String TIME_FORMAT = "HH:mm:ss";

    //changed to public so i can access it in my Transaction class for toString
    //To format and parse dates based on date format constant
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    //changed to public so i can access it in my Transaction class for toString
    //to format and parse times based on the time format constant
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);//changed to public so i can access it in my Transaction class for toString

    //Main method entry point in application
    public static void main(String[] args) {
        loadTransactions(FILE_NAME); //Loads transactions from the file

        Collections.sort(transactions, Comparator.comparing(Transaction::getDate).reversed()); //sorts transactions by date, with reversed it will do newest to oldest

        //Creates a scanner object to read user input
        Scanner scanner = new Scanner(System.in);

        //Starts loop by setting it to true
        boolean running = true;

        //loop to keep displaying the menu
        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            //reads user's input and removes extra spaces
            String input = scanner.nextLine().trim();


            //Switch statement to handle user input
            switch (input.toUpperCase()) {
                case "D":
                    //Calling method to add a deposit
                    addDeposit(scanner);
                    break;
                case "P":
                    //calling method to add a payment
                    addPayment(scanner);
                    break;
                case "L":
                    //Calling method to open ledger
                    ledgerMenu(scanner);
                    break;
                //Exits application
                case "X":
                    System.out.println("Exiting from application!");
                    running = false;
                    break;
                default:
                    //handles invalid options
                    System.out.println("Invalid option");
                    break;
            }
        }
        //closing scanner
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

            //sorts transactions by date, with reversed it will do newest to oldest
            Collections.sort(transactions, Comparator.comparing(Transaction::getDate).reversed());

        } catch (Exception e) {
            System.out.println("Error has occurred!"); //if any errors happen, print message
            // print stack trace if exception occurs
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

            //sorts transactions by date, with reversed it will do newest to oldest
            Collections.sort(transactions, Comparator.comparing(Transaction::getDate).reversed());

        } catch (Exception e) {
            System.out.println("Invalid. Try again!");
        }
    }

    //Method to add payments to file
    private static void addPayment(Scanner scanner) {
        LocalDate date = LocalDate.now(); //defaults to current date
        LocalTime time = LocalTime.now(); //defaults to current time


        //checks if a valid date and time have been entered
        boolean correctDateTime = false;


        //Loop until valid date and time input
        while(!correctDateTime) {


            //This try is for validating date and time
            try {
                System.out.println("Please enter the Date and Time in the following format: (yyyy-MM-dd HH:mm:ss)");
                String dateTimeInput = scanner.nextLine().trim(); //read and trims the input

                String[] parts = dateTimeInput.split(" "); //splits the input into date and time parts
                date = LocalDate.parse(parts[0], DATE_FORMATTER); // parse date string using custom formatter
                time = LocalTime.parse(parts[1], TIME_FORMATTER);// parse time using custom formatter

                //Exit loop if no error
                correctDateTime = true;

                //sorts transactions by date, with reversed it will do newest to oldest
                Collections.sort(transactions, Comparator.comparing(Transaction::getDate).reversed());

            } catch (Exception e) {
                //if error occurs, loop again
                System.out.println("Invalid Date and Time format. Please try again.");
            }

        }

        //This try is for handling input and file writing
        try{
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

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true)); //By adding true it keeps existing data and puts new transaction at the bottom
            String line = newPayment.toString(); //call toString method on the newPayment
            writer.write(line); //writes the string of the current transaction
            writer.newLine(); // clearing line

            writer.close(); //closes writer


        } catch (Exception e) {
            System.out.println("Invalid input.");
            // print stack trace if exception occurs
            e.printStackTrace();
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        //Starts loop by setting it to true
        boolean running = true;

        //loop to keep displaying the menu
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            //reads user input for the selected search option
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
            // print stack trace if exception occurs
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
            //sorts transactions by date, with reversed it will do newest to oldest
            Collections.sort(transactions, Comparator.comparing(Transaction::getDate).reversed());

        } catch (Exception e) {
            System.out.println("Error has occurred while displaying deposits.");
            // print stack trace if exception occurs
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
            // print stack trace if exception occurs
            e.printStackTrace();
        }
    }

    private static void reportsMenu(Scanner scanner) {
        //Starts loop by setting it to true
        boolean running = true;

        //loop to keep displaying the menu
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


            //reads user input for the selected search option
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

                    //Gets today's date as end of range
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
                    customSearch(scanner);
                    //this will be for custom search
                  //  customSearchMenu(scanner); This is what I had originally for first attempt at challenge, it would take the user to a new sub menu
                    break;

                case "0":
                    //Exit the loop
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
            // print stack trace if exception occurs
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
            // print stack trace if exception occurs
            e.printStackTrace();
        }
    }
    private static void customSearch(Scanner scanner) {
        System.out.println("Custom Search");

        System.out.println("Enter the Start Date (yyyy-MM-dd) Or press enter to skip!");
        String startDateInput = scanner.nextLine().trim();

        System.out.println("Enter the End Date (yyyy-MM-dd) Or press enter to skip!");
        String endDateInput = scanner.nextLine().trim();

        System.out.println("Enter the Description or press enter to skip!");
        String description = scanner.nextLine().trim();

        System.out.println("Enter the Vendor or press enter to skip!");
        String vendor = scanner.nextLine().trim();

        System.out.println("Enter the Amount or press enter to skip!");
        String amountInput = scanner.nextLine().trim();

        boolean results = false;

        try {
            //Loop through each transaction in list
            for (Transaction transaction : transactions) {

                //if start date input is not empty
                if (!startDateInput.isEmpty()) {

                    //parse the start date from input string into LocalDate object
                    LocalDate startDate = LocalDate.parse(startDateInput, DATE_FORMATTER);
                    //Checks if transaction date is before the input start date
                    if (transaction.getDate().isBefore(startDate))
                        //Skips this and moves onto next part of loop
                        continue;
                }
                //if end date input is not empty
                if (!endDateInput.isEmpty()) {
                    //parse the end date from input string into LocalDate Object
                    LocalDate endDate = LocalDate.parse(endDateInput, DATE_FORMATTER);
                    //checks if transaction date is after the input end date
                    if (transaction.getDate().isAfter(endDate))
                        //skips this and moves onto next part of loop
                        continue;
                }
                //if input entered a description AND the transaction's description does NOT match
                if (!description.isEmpty() && !transaction.getDescription().equalsIgnoreCase(description))
                    //skips this and moves onto next part of loop
                    continue;

                //if input entered a vendor AND the transaction's vendor does NOT match
                if (!vendor.isEmpty() && !transaction.getVendor().equalsIgnoreCase(vendor))
                    //skips this and moves onto the next part of the loop
                    continue;

                //if input entered is an amount
                if (!amountInput.isEmpty()) {
                    //change input from a string to a double
                    double amount = Double.parseDouble(amountInput);
                    //if transaction amount does NOT match entered amount
                    if (transaction.getAmount() != amount)
                        //skips this and moves onto next part of the loop
                        continue;
                }
                //if all checks pass, print transaction
                System.out.println(transaction);
                results = true;
            }
            //after checking all transactions and no matching results were found
            if (!results) {
                System.out.println("No transactions matched your search.");

            }
            //catch any exceptions
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }
    //Below was my first attempt at the challenge, but I misunderstood it!
 /*
    //Method for custom search menu
    private static void customSearchMenu(Scanner scanner) {
        //Starts loop by setting it to true
        boolean running = true;

        //loop to keep displaying the menu
        while (running) {

            //displaying custom search menu option
            System.out.println("Custom Search");
            System.out.println("Choose an option:");
            System.out.println("1) Search by Date Range");
            System.out.println("2) Search by Vendor");
            System.out.println("3) Search by Description");
            System.out.println("4) Search by Amount");
            System.out.println("0) Back");

            //reads user input for the selected search option
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    //asks user for custom date range
                    try {
                        System.out.println("Enter the Start Date (yyyy-MM-dd): ");

                        //parse the start date from user input
                        LocalDate startDate = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);

                        System.out.println("Enter the End Date (yyyy-MM-dd): ");

                        //parse end date from user input
                        LocalDate endDate = LocalDate.parse(scanner.nextLine().trim(),DATE_FORMATTER);

                        //Calling method to filter transactions by date
                        filterTransactionsByDate(startDate, endDate);

                    //Print error message if date entered is invalid
                    } catch (Exception e) {
                        System.out.println("Invalid date format. Please use yyyy-MM-dd");
                    }

                    break;

                case "2":
                    //asks user for vendor name
                    System.out.println("Enter the Vendor Name:");

                    //takes user input for vendor name
                    String vendor = scanner.nextLine().trim();

                    //Calling method to filter transactions by vendor
                    filterTransactionsByVendor(vendor);

                    break;

                case "3":
                    //Asks user for custom description
                    System.out.println("Enter the Description: ");

                    //takes user input for description
                    String description = scanner.nextLine().trim();

                    //Calling method to filter transactions by description
                    filterTransactionsByDescription(description);

                    break;

                case "4":
                    //Asks user for custom amount
                    System.out.println("Enter the Amount: ");

                    //parse amount from user input as a double
                    double amount = Double.parseDouble(scanner.nextLine().trim());

                    //calling method to filter transactions by amount
                    filterTransactionsByAmount(amount);
                    break;

                case "0":
                    //Exit the loop
                    running = false;
                    break;

                default:
                    //prints message if user selects invalid option
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
//Method for filtering transactions by description
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
            //if no matching transactions were found, print message
            if (!results) {
                System.out.println("No transactions were found with the description: " + description);
            }
        } catch (Exception e) {
            // print stack trace if exception occurs
            e.printStackTrace();
        }
    }

    //Method for filtering transactions by amount
    private static void filterTransactionsByAmount(double amount) {
        //Checking if any results were found
        boolean results = false;
        try {
            //Loop through all transactions in list
            for (Transaction transaction : transactions) {
                //Checks if the transaction amount matches input amount
                if (transaction.getAmount() == amount) {
                    System.out.println(transaction);

                    //changes to true is match is found
                    results = true;
                }
            }
            // if no matching transactions amount were found
            if (!results) {
                System.out.println("No transactions were found with the amount: " + amount);
            }
        } catch (Exception e) {
            // print stack trace if exception occurs
            e.printStackTrace();
        }

  */
    }

